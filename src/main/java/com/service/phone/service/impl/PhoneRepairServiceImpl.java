package com.service.phone.service.impl;

import com.service.phone.dto.PhoneRepairAnswerDTO;
import com.service.phone.dto.PhoneRepairDTO;
import com.service.phone.dto.PhoneRepairRequestAnswerDTO;
import com.service.phone.dto.UserAnswerDTO;
import com.service.phone.enumeration.Status;
import com.service.phone.exception.PhoneNumberDoesNotExistException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.model.for_phone.PhoneRepair;
import com.service.phone.model.for_phone.PhoneRepairRequest;
import com.service.phone.model.for_phone.Photo;
import com.service.phone.model.user.User;
import com.service.phone.repository.PhoneRepairRepository;
import com.service.phone.repository.PhoneRepairRequestRepository;
import com.service.phone.service.PhoneRepairRequestService;
import com.service.phone.service.PhoneRepairService;
import com.service.phone.service.RoomService;
import com.service.phone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PhoneRepairServiceImpl implements PhoneRepairService {

    private final PhoneRepairRequestService phoneRepairRequestService;
    private final PhoneRepairRequestRepository phoneRepairRequestRepository;
    private final PhoneRepairRepository phoneRepairRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Override
    @Transactional
    public void registration(PhoneRepairDTO phoneRepairDTO) throws PhoneNumberDoesNotExistException, UsernameExistException {
        PhoneRepairRequest phoneRepairRequest = phoneRepairRequestRepository.findByIdApp(phoneRepairDTO.getIdApp()).get();
        User engineer = userService.findByUsername(phoneRepairDTO.getEngineer());
        phoneRepairRequest.setStatus(Status.ACTIVE.name());
        PhoneRepair phoneRepair = new PhoneRepair();
        phoneRepair.setStatus(Status.ACTIVE.name());
        phoneRepair.setEngineer(engineer);
        phoneRepairRequest.addPhoneRepair(phoneRepair);
        roomService.createRoom(phoneRepair);
    }

    @Override
    public Set<PhoneRepairAnswerDTO> getPhoneRepairForCustomer(String username) {
        Set<PhoneRepair> phoneRepairs = phoneRepairRepository
                .findByPhoneRepairRequestCustomerUsername(username).orElse(new HashSet<>())
                .stream().filter(p -> p.getStatus().equals(Status.ACTIVE.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairAnswerDTOs(phoneRepairs);
    }

    @Override
    public Set<PhoneRepairAnswerDTO> getPhoneRepairForEngineer(String username) {
        Set<PhoneRepair> phoneRepairs = phoneRepairRepository
                .findByEngineerUsername(username).orElse(new HashSet<>())
                .stream().filter(p -> p.getStatus().equals(Status.ACTIVE.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairAnswerDTOs(phoneRepairs);
    }

    @Override
    public void ready(PhoneRepairDTO phoneRepairDTO) throws UsernameExistException {
        PhoneRepair phoneRepair = phoneRepairRepository.findByPhoneRepairRequestIdApp(phoneRepairDTO.getIdApp()).get();
        phoneRepair.getPhoneRepairRequest().setStatus(Status.READY.name());
        phoneRepair.setStatus(Status.READY.name());
        phoneRepairRepository.save(phoneRepair);
    }

    @Override
    public Set<PhoneRepairAnswerDTO> getForCustomerOnReady(String username) {
        Set<PhoneRepair> phoneRepairs = phoneRepairRepository
                .findByPhoneRepairRequestCustomerUsername(username).orElse(new HashSet<>())
                .stream().filter(p -> p.getStatus().equals(Status.READY.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairAnswerDTOs(phoneRepairs);
    }

    @Override
    public Set<PhoneRepairAnswerDTO> getForEngineerOnReady(String username) {
        Set<PhoneRepair> phoneRepairs = phoneRepairRepository
                .findByEngineerUsername(username).orElse(new HashSet<>())
                .stream().filter(p -> p.getStatus().equals(Status.READY.name()))
                .collect(Collectors.toSet());
        return createPhoneRepairAnswerDTOs(phoneRepairs);
    }

    private Set<PhoneRepairAnswerDTO> createPhoneRepairAnswerDTOs(Set<PhoneRepair> phoneRepairs) {
        Set<PhoneRepairAnswerDTO> phoneRepairAnswerDTOs = new HashSet<>();
        phoneRepairs.forEach(p -> {
            PhoneRepairAnswerDTO phoneRepairAnswerDTO = new PhoneRepairAnswerDTO();
            phoneRepairAnswerDTO.setRoomNumber(p.getRoom().getNumber());
            phoneRepairAnswerDTO.setDateOfAcceptanceOfTheRequest(p.getDateOfAcceptanceOfTheRequest());
            phoneRepairAnswerDTO.setEngineer(p.getEngineer().getUsername());
            PhoneRepairRequestAnswerDTO phoneRepairRequestAnswerDTO = new PhoneRepairRequestAnswerDTO();
            phoneRepairRequestAnswerDTO.setPhoneNumber(p.getPhoneRepairRequest().getPhoneNumber());
            phoneRepairRequestAnswerDTO.setIdApp(p.getPhoneRepairRequest().getIdApp());
            phoneRepairRequestAnswerDTO.setCauseOfFailure(p.getPhoneRepairRequest().getCauseOfFailure());
            phoneRepairRequestAnswerDTO.setDateOfRequestForPhoneRepair(p.getPhoneRepairRequest().getDateOfRequestForPhoneRepair());
            phoneRepairRequestAnswerDTO.setAllPhoto(p.getPhoneRepairRequest().getPhotos().stream().map(Photo::getUrlPhoto).collect(Collectors.toSet()));
            UserAnswerDTO customer = new UserAnswerDTO();
            customer.setUsername(p.getPhoneRepairRequest().getCustomer().getUsername());
            customer.setEmail(p.getPhoneRepairRequest().getCustomer().getEmail());
            customer.setRole(p.getPhoneRepairRequest().getCustomer().getRole());
            customer.setAuthorities(p.getPhoneRepairRequest().getCustomer().getAuthorities());
            phoneRepairRequestAnswerDTO.setCustomer(customer);
            phoneRepairAnswerDTO.setPhoneRepairRequestAnswerDTO(phoneRepairRequestAnswerDTO);
            phoneRepairAnswerDTOs.add(phoneRepairAnswerDTO);
        });
        return phoneRepairAnswerDTOs;
    }

}
