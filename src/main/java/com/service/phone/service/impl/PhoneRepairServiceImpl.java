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
    private final PhoneRepairRepository phoneRepairRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Override
    @Transactional
    public void registration(PhoneRepairDTO phoneRepairDTO) throws PhoneNumberDoesNotExistException, UsernameExistException {
        PhoneRepairRequest phoneRepairRequest = phoneRepairRequestService.findByPhoneNumber(phoneRepairDTO.getPhoneNumber());
        User engineer = userService.findByUsername(phoneRepairDTO.getEngineer());
        PhoneRepair phoneRepair = new PhoneRepair();
        phoneRepair.setPhoneRepairRequest(phoneRepairRequest);
        phoneRepair.setEngineer(engineer);
        phoneRepair.setStatus(Status.ACTIVE.name());
        phoneRepair = phoneRepairRepository.save(phoneRepair);
        roomService.createRoom(phoneRepair);
    }

    @Override
    public Set<PhoneRepairAnswerDTO> getPhoneRepairForCustomer(String username) {
        Set<PhoneRepair> phoneRepairs = phoneRepairRepository
                .findByPhoneRepairRequestCustomerUsername(username).orElse(new HashSet<>());
        return createPhoneRepairAnswerDTOs(phoneRepairs);
    }

    @Override
    public Set<PhoneRepairAnswerDTO> getPhoneRepairForEngineer(String username) {
        Set<PhoneRepair> phoneRepairs = phoneRepairRepository
                .findByEngineerUsername(username).orElse(new HashSet<>());
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
