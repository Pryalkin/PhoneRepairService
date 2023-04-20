package com.service.phone.constant;

import com.service.phone.dto.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpAnswer {

    public static final String USER_SUCCESSFULLY_REGISTERED = "User successfully registered";
    public static final String APPLICATION_SUCCESSFULLY_SENT = "Application successfully sent";
    public static final String APPLICATION_SUCCESSFULLY_ACCEPTED = "Application successfully accepted";

    public static ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        HttpResponse body = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase());
        return new ResponseEntity<>(body, httpStatus);
    }
}
