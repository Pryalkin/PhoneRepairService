package com.service.phone.service;

import com.service.phone.dto.UserDTO;
import com.service.phone.exception.EmailExistException;
import com.service.phone.exception.PasswordException;
import com.service.phone.exception.UsernameExistException;
import com.service.phone.model.user.User;

public interface UserService {
    void registration(UserDTO userDTO) throws UsernameExistException, EmailExistException, PasswordException;
    User findByUsername(String username) throws UsernameExistException;
}
