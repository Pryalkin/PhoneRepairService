package com.service.phone.service;

import com.service.phone.dto.PhoneRepairRequestAnswerDTO;
import com.service.phone.exception.PhoneNumberDoesNotExistException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.model.for_phone.PhoneRepairRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface PhoneRepairRequestService {
    void create(String phoneNumber, String causeOfFailure, String username, MultipartFile photo1, MultipartFile photo2, MultipartFile photo3) throws UsernameExistException, IOException;

    Set<PhoneRepairRequestAnswerDTO> getInactiveForEngineer();

    PhoneRepairRequest findByPhoneNumber(String phoneNumber) throws PhoneNumberDoesNotExistException;

    Set<PhoneRepairRequestAnswerDTO> getInactive(String username);

    Set<PhoneRepairRequestAnswerDTO> getActive(String username);

    PhoneRepairRequestAnswerDTO getDetails(String idApp);
}
