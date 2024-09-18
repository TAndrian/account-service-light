package com.learning.account_service.exception;

public class NotFoundException extends GlobalException {
    public NotFoundException(String message, String code) {
        super(message, code);
    }
}
