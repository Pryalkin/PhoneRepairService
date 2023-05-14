package com.service.phone.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class PhoneRepairRequestAnswerDTO {

    private String idApp;
    private String phoneNumber;
    private String causeOfFailure;
    private Set<String> photos = new HashSet<>();
    private UserAnswerDTO customer;
    private LocalDateTime dateOfRequestForPhoneRepair;

    public void setAllPhoto(Set<String> photos){
        photos.addAll(photos);
    }

}
