package com.service.phone.service.impl;

import com.service.phone.dto.PhoneRepairRequestAnswerDTO;
import com.service.phone.dto.UserAnswerDTO;
import com.service.phone.enumeration.Status;
import com.service.phone.exception.PhoneNumberDoesNotExistException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.model.for_phone.PhoneRepairRequest;
import com.service.phone.model.for_phone.Photo;
import com.service.phone.model.user.User;
import com.service.phone.repository.PhoneRepairRequestRepository;
import com.service.phone.service.PhoneRepairRequestService;
import com.service.phone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.service.phone.constant.ExceptionConstant.THIS_PHONE_NUMBER_DOES_NOT_EXIST;
import static com.service.phone.constant.FileConstant.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@AllArgsConstructor
@Slf4j
public class PhoneRepairRequestServiceImpl implements PhoneRepairRequestService {

    private final PhoneRepairRequestRepository phoneRepairRequestRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void create(String phoneNumber, String causeOfFailure, String username, MultipartFile photo1, MultipartFile photo2, MultipartFile photo3) throws UsernameExistException, IOException {
        User customer = userService.findByUsername(username);
        PhoneRepairRequest phoneRepairRequest = new PhoneRepairRequest();
        phoneRepairRequest.setPhoneNumber(phoneNumber);
        String idApp = generateName();
        while(phoneRepairRequestRepository.findByIdApp(idApp).isPresent()){
            idApp = generateName();
        }
        phoneRepairRequest.setIdApp(idApp);
        phoneRepairRequest.setCauseOfFailure(causeOfFailure);
        phoneRepairRequest.setCustomer(customer);
        phoneRepairRequest.setStatus(Status.INACTIVE.name());
        phoneRepairRequest = savePhoto(username, photo1, phoneRepairRequest);
        phoneRepairRequest = savePhoto(username, photo2, phoneRepairRequest);
        phoneRepairRequest = savePhoto(username, photo3, phoneRepairRequest);
        phoneRepairRequest.setDateOfRequestForPhoneRepair(LocalDateTime.now());
        phoneRepairRequestRepository.save(phoneRepairRequest);
    }

    @Override
    public Set<PhoneRepairRequestAnswerDTO> getInactiveForEngineer() {
        Set<PhoneRepairRequest> phoneRepairRequests = phoneRepairRequestRepository.findAll()
                .stream().filter(p -> p.getStatus().equals(Status.INACTIVE.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairRequestAnswerDTOs(phoneRepairRequests);

    }

    @Override
    public PhoneRepairRequest findByPhoneNumber(String phoneNumber) throws PhoneNumberDoesNotExistException {
        return phoneRepairRequestRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new PhoneNumberDoesNotExistException(THIS_PHONE_NUMBER_DOES_NOT_EXIST));
    }

    @Override
    public Set<PhoneRepairRequestAnswerDTO> getInactive(String username) {
        Set<PhoneRepairRequest> phoneRepairRequests = phoneRepairRequestRepository.findByCustomerUsername(username)
                .stream()
                .filter(p -> p.getStatus().equals(Status.INACTIVE.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairRequestAnswerDTOs(phoneRepairRequests);
    }

    @Override
    public Set<PhoneRepairRequestAnswerDTO> getActive(String username) {
        Set<PhoneRepairRequest> phoneRepairRequests = phoneRepairRequestRepository.findByCustomerUsername(username)
                .stream()
                .filter(p -> p.getStatus().equals(Status.ACTIVE.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairRequestAnswerDTOs(phoneRepairRequests);
    }

    @Override
    public PhoneRepairRequestAnswerDTO getDetails(String idApp) {
        return createPhoneRepairRequestAnswerDTO(phoneRepairRequestRepository.findByIdApp(idApp).get());
    }

    private PhoneRepairRequest savePhoto(String username, MultipartFile photo, PhoneRepairRequest phoneRepairRequest) throws IOException {
        if (photo != null){
            Path userFolder = Paths.get(USER_FOLDER + username).toAbsolutePath().normalize();
            if (!Files.exists(userFolder)){
                Files.createDirectories(userFolder);
            }
            String name = generateName();
            Files.deleteIfExists(Paths.get(userFolder + FORWARD_SLASH + name +  DOT + JPG_EXTENSION));
            Files.copy(photo.getInputStream(), userFolder.resolve(name + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            Photo ph = new Photo();
            ph.setUrlPhoto(setLogoImageUrl(username, name));
            phoneRepairRequest.addPhoto(ph);
            return phoneRepairRequest;
        }
        return phoneRepairRequest;
    }

    private String setLogoImageUrl(String username, String name) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path(USER_IMAGE_PATH + username + FORWARD_SLASH + name + DOT + JPG_EXTENSION).toUriString();
    }

    private Set<PhoneRepairRequestAnswerDTO> createPhoneRepairRequestAnswerDTOs(Set<PhoneRepairRequest> phoneRepairRequests) {
        Set<PhoneRepairRequestAnswerDTO> phoneRepairRequestAnswerDTOs = new HashSet<>();
        phoneRepairRequests.forEach(p -> {
            phoneRepairRequestAnswerDTOs.add(createPhoneRepairRequestAnswerDTO(p));
        });
        return phoneRepairRequestAnswerDTOs;
    }

    private PhoneRepairRequestAnswerDTO createPhoneRepairRequestAnswerDTO(PhoneRepairRequest p) {
        PhoneRepairRequestAnswerDTO phoneRepairRequestAnswerDTO = new PhoneRepairRequestAnswerDTO();
        phoneRepairRequestAnswerDTO.setPhoneNumber(p.getPhoneNumber());
        phoneRepairRequestAnswerDTO.setIdApp(p.getIdApp());
        phoneRepairRequestAnswerDTO.setCauseOfFailure(p.getCauseOfFailure());
        phoneRepairRequestAnswerDTO.setDateOfRequestForPhoneRepair(p.getDateOfRequestForPhoneRepair());
        phoneRepairRequestAnswerDTO.getPhotos()
                .addAll(p.getPhotos().stream().map(Photo::getUrlPhoto).collect(Collectors.toSet()));
        UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
        userAnswerDTO.setUsername(p.getCustomer().getUsername());
        userAnswerDTO.setEmail(p.getCustomer().getEmail());
        userAnswerDTO.setRole(p.getCustomer().getRole());
        userAnswerDTO.setAuthorities(p.getCustomer().getAuthorities());
        phoneRepairRequestAnswerDTO.setCustomer(userAnswerDTO);
        return phoneRepairRequestAnswerDTO;
    }

    private String generateName() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
