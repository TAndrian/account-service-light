package com.learning.account_service.exception.enums.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserExceptionEnums {
    USER_EXCEPTION_CODE("v1.user.resource"),
    USER_ALREADY_EXISTS("User already exists"),
    USER_NOT_FOUND("User not found");
    private final String value;
}
