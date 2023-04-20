package com.service.phone.controller;

import com.service.phone.constant.HttpAnswer;
import com.service.phone.dto.HttpResponse;
import com.service.phone.dto.PhoneRepairAnswerDTO;
import com.service.phone.dto.PhoneRepairDTO;
import com.service.phone.exception.ExceptionHandling;
import com.service.phone.exception.NoRightException;
import com.service.phone.exception.PhoneNumberDoesNotExistException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.service.PhoneRepairService;
import com.service.phone.utility.JWTTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.service.phone.constant.HttpAnswer.APPLICATION_SUCCESSFULLY_ACCEPTED;
import static com.service.phone.controller.security.ValidUsernameSecurity.checkUsernameForValidity;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/phone_repair")
@AllArgsConstructor
public class PhoneRepairController extends ExceptionHandling {

    private final PhoneRepairService phoneRepairService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<HttpResponse> registration(@RequestBody PhoneRepairDTO phoneRepairDTO) throws PhoneNumberDoesNotExistException, UsernameExistException {
        phoneRepairService.registration(phoneRepairDTO);
        return HttpAnswer.response(CREATED, APPLICATION_SUCCESSFULLY_ACCEPTED);
    }

    @GetMapping("/get/for_customer")
    public ResponseEntity<Set<PhoneRepairAnswerDTO>> getForCustomer(@RequestParam String username,
                                                                    HttpServletRequest request) throws NoRightException {
        checkUsernameForValidity(request, jwtTokenProvider, username);
        return new ResponseEntity<>(phoneRepairService.getPhoneRepairForCustomer(username), OK);
    }

    @GetMapping("/get/for_engineer")
    public ResponseEntity<Set<PhoneRepairAnswerDTO>> getForEngineer(@RequestParam String username,
                                                                    HttpServletRequest request) throws NoRightException {
        checkUsernameForValidity(request, jwtTokenProvider, username);
        return new ResponseEntity<>(phoneRepairService.getPhoneRepairForEngineer(username), OK);
    }


}
