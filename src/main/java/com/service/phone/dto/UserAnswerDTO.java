package com.service.phone.dto;

import lombok.Data;

@Data
public class UserAnswerDTO {

    private String username;
    private String email;
    private String role;
    private String[] authorities;

}
