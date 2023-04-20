package com.service.phone.service;

import com.service.phone.dto.PhoneRepairAnswerDTO;
import com.service.phone.dto.PhoneRepairDTO;
import com.service.phone.exception.PhoneNumberDoesNotExistException;
import com.service.phone.exception.UsernameExistException;

import java.util.Set;

public interface PhoneRepairService {
    void registration(PhoneRepairDTO phoneRepairDTO) throws PhoneNumberDoesNotExistException, UsernameExistException;

    Set<PhoneRepairAnswerDTO> getPhoneRepairForCustomer(String username);

    Set<PhoneRepairAnswerDTO> getPhoneRepairForEngineer(String username);
}
