package com.service.phone.controller;

import com.service.phone.constant.HttpAnswer;
import com.service.phone.dto.HttpResponse;
import com.service.phone.dto.UserAnswerDTO;
import com.service.phone.dto.UserDTO;
import com.service.phone.exception.EmailExistException;
import com.service.phone.exception.ExceptionHandling;
import com.service.phone.exception.PasswordException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.model.user.User;
import com.service.phone.model.user.UserPrincipal;
import com.service.phone.service.UserService;
import com.service.phone.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.service.phone.constant.FileConstant.*;
import static com.service.phone.constant.HttpAnswer.USER_SUCCESSFULLY_REGISTERED;
import static com.service.phone.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
//@CrossOrigin(origins = "*")
public class UserController extends ExceptionHandling {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<HttpResponse> registration(@RequestBody UserDTO userDTO) throws EmailExistException, UsernameExistException, PasswordException {
        userService.registration(userDTO);
        return HttpAnswer.response(CREATED, USER_SUCCESSFULLY_REGISTERED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserAnswerDTO> login(@RequestBody UserDTO userDTO) throws UsernameExistException {
        authenticate(userDTO.getUsername(), userDTO.getPassword());
        User loginUser = userService.findByUsername(userDTO.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
        userAnswerDTO.setUsername(loginUser.getUsername());
        userAnswerDTO.setEmail(loginUser.getEmail());
        userAnswerDTO.setRole(loginUser.getRole());
        userAnswerDTO.setAuthorities(loginUser.getAuthorities());
        return new ResponseEntity<>(userAnswerDTO, jwtHeader, OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username,
                                  @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }
}
