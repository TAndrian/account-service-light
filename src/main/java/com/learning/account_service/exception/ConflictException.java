package com.learning.account_service.exception;

public class ConflictException extends GlobalException {
    public ConflictException(String message, String code) {
        super(message, code);
    }
}
