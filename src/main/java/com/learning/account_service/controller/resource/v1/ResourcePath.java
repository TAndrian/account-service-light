package com.learning.account_service.controller.resource.v1;

public class ResourcePath {

    public static final String BASE_URL = "/api/v1";
    public static final String USERS = "/users";
    public static final String USER = "/user";
    public static final String UUID_PATTERN = "^[0-9a-fA-F\\-]{36}$";
    public static final String USER_ID = "/{userId:" + UUID_PATTERN + "}";

    public static final String PATH_USER_ID = USER + USER_ID;

    private ResourcePath() {
        throw new IllegalStateException("Utility class");
    }
}
