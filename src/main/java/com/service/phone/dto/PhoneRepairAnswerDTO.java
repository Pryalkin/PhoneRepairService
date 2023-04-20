package com.service.phone.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PhoneRepairAnswerDTO {

    private LocalDateTime dateOfAcceptanceOfTheRequest;
    private String engineer;
    private PhoneRepairRequestAnswerDTO phoneRepairRequestAnswerDTO;
    private String roomNumber;

}
