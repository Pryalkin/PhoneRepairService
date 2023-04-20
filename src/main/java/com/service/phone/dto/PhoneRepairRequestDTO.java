package com.service.phone.dto;

import com.service.phone.model.for_phone.Photo;
import com.service.phone.model.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Data
public class PhoneRepairRequestDTO {

    private String phoneNumber;
    private String causeOfFailure;
    private Set<MultipartFile> photos;
    private String username;
}
