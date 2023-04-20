package com.service.phone.exception;

public class PhoneNumberDoesNotExistException extends Exception{
    public PhoneNumberDoesNotExistException(String message) {
        super(message);
    }
}