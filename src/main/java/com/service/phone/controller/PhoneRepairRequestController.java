package com.service.phone.controller;

import com.service.phone.constant.HttpAnswer;
import com.service.phone.dto.HttpResponse;
import com.service.phone.dto.PhoneRepairRequestAnswerDTO;
import com.service.phone.exception.*;
import com.service.phone.service.PhoneRepairRequestService;
import com.service.phone.utility.JWTTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

import static com.service.phone.constant.HttpAnswer.APPLICATION_SUCCESSFULLY_SENT;
import static com.service.phone.constant.SecurityConstant.TOKEN_PREFIX;
import static com.service.phone.controller.security.ValidUsernameSecurity.checkUsernameForValidity;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/phone_repair_request")
@AllArgsConstructor
public class PhoneRepairRequestController extends ExceptionHandling {

    private final PhoneRepairRequestService phoneRepairRequestService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> registration(@RequestParam String phoneNumber,
                                                     @RequestParam String causeOfFailure,
                                                     @RequestParam String username,
                                                     @RequestParam(value = "photo1", required = false) MultipartFile photo1,
                                                     @RequestParam(value = "photo2", required = false) MultipartFile photo2,
                                                     @RequestParam(value = "photo3", required = false) MultipartFile photo3,
                                                     HttpServletRequest request) throws NoRightException, IOException, UsernameExistException {
        checkUsernameForValidity(request, jwtTokenProvider, username);
        phoneRepairRequestService.create(phoneNumber, causeOfFailure, username, photo1, photo2, photo3);
        return HttpAnswer.response(CREATED, APPLICATION_SUCCESSFULLY_SENT);
    }

    @GetMapping("/engineer/get/inactive")
    @PreAuthorize("hasAnyAuthority('user:get_inactive')")
    public ResponseEntity<Set<PhoneRepairRequestAnswerDTO>> getInactiveForEngineer() {
        return new ResponseEntity<>(phoneRepairRequestService.getInactiveForEngineer(), OK);
    }

    @GetMapping("/user/get/inactive")
    public ResponseEntity<Set<PhoneRepairRequestAnswerDTO>> getInactive(HttpServletRequest request) {
        return new ResponseEntity<>(phoneRepairRequestService.getInactive(getUsernameWithToken(request)), OK);
    }

    @GetMapping("/user/get/active")
    public ResponseEntity<Set<PhoneRepairRequestAnswerDTO>> getActive(HttpServletRequest request) {
        return new ResponseEntity<>(phoneRepairRequestService.getActive(getUsernameWithToken(request)), OK);
    }

    @GetMapping("/user/get/details/{idApp}")
    public ResponseEntity<PhoneRepairRequestAnswerDTO> getDetail(@PathVariable String idApp) {
        return new ResponseEntity<>(phoneRepairRequestService.getDetails(idApp), OK);
    }

    private String getUsernameWithToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.substring(TOKEN_PREFIX.length());
        return jwtTokenProvider.getSubject(token);
    }
}
