package com.service.phone.service.impl;

import com.service.phone.dto.UserDTO;
import com.service.phone.exception.EmailExistException;
import com.service.phone.exception.PasswordException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.model.user.User;
import com.service.phone.model.user.UserPrincipal;
import com.service.phone.repository.UserRepository;
import com.service.phone.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.service.phone.constant.UserImplConstant.*;
import static com.service.phone.enumeration.Role.*;

@AllArgsConstructor
@Service
@Qualifier("userDetailsService")
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        log.info(FOUND_USER_BY_USERNAME + username);
        return userPrincipal;
    }

    @Override
    public void registration(UserDTO userDTO) throws UsernameExistException, EmailExistException, PasswordException {
        validateNewUsernameAndEmailAndPassword(userDTO);
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodePassword(userDTO.getPassword()));
        if (userRepository.findAll().isEmpty()) {
            user.setRole(ROLE_ENGINEER.name());
            user.setAuthorities(ROLE_ENGINEER.getAuthorities());
        } else {
            user.setRole(ROLE_USER.name());
            user.setAuthorities(ROLE_USER.getAuthorities());
        }
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) throws UsernameExistException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameExistException(USERNAME_ALREADY_EXISTS));
    }

    private void validateNewUsernameAndEmailAndPassword(UserDTO userDTO) throws UsernameExistException, EmailExistException, PasswordException {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()){
            throw new UsernameExistException(USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()){
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
        if (!userDTO.getPassword().equals(userDTO.getPassword2())){
            throw new PasswordException(PASSWORD_IS_NOT_VALID);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
