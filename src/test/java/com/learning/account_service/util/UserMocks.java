package com.learning.account_service.util;

import com.learning.account_service.business.enums.Role;
import com.learning.account_service.business.enums.UserEntity;
import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UpdateUserDTO;
import com.learning.account_service.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class UserMocks {
    public static final UUID MOCK_USER_ID = UUID.randomUUID();
    public static final UUID MOCK_NOT_FOUND_USER_ID = UUID.randomUUID();
    public static final String JOHN = "John";
    public static final String SARAH = "Sarah";
    public static final String CONNOR = "Connor";
    public static final String JOHN_CONNOR_EMAIL = "john.connor@test.com";
    public static final String PASSWORD = "PASSWORD";

    public static final List<UserEntity> MOCK_USER_ENTITIES = UserMocks.userEntities();
    public static final Set<UserDTO> MOCK_USER_DTOs = UserMocks.userDTOs();
    public static final UserEntity MOCK_USER_ENTITY = UserMocks.userEntity();
    public static final UserDTO MOCK_USER_DTO = UserMocks.userDTO();
    public static final CreateUserDTO MOCK_CREATE_USER_DTO = UserMocks.createUserDTO();
    public static final UserEntity MOCK_UPDATED_USER_ENTITY = UserMocks.updatedUserEntity();
    public static final UpdateUserDTO MOCK_UPDATE_USER_DTO = UserMocks.updateUserDTO();
    public static final UserDTO MOCK_UPDATED_USER_DTO = UserMocks.updatedUserDTO();
    public static final CreateUserDTO MOCK_INVALID_USER_TO_CREATE_DTO = UserMocks.invalidCreateUserDTO();


    public static UserEntity userEntity() {
        return UserEntity.builder()
                .id(MOCK_USER_ID)
                .firstname(JOHN)
                .lastname(CONNOR)
                .email(JOHN_CONNOR_EMAIL)
                .role(Role.USER)
                .build();
    }

    public static UserDTO userDTO() {
        return new UserDTO(
                MOCK_USER_ID,
                JOHN,
                CONNOR,
                JOHN_CONNOR_EMAIL,
                Role.USER
        );
    }

    public static List<UserEntity> userEntities() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity());
        return userEntities;
    }

    public static Set<UserDTO> userDTOs() {
        Set<UserDTO> userDTOs = new HashSet<>();
        userDTOs.add(userDTO());
        return userDTOs;
    }

    public static CreateUserDTO createUserDTO() {
        return new CreateUserDTO(
                JOHN,
                CONNOR,
                JOHN_CONNOR_EMAIL,
                PASSWORD,
                Role.USER
        );
    }

    public static CreateUserDTO invalidCreateUserDTO() {
        return new CreateUserDTO(
                "",
                CONNOR,
                JOHN_CONNOR_EMAIL,
                "",
                Role.USER
        );
    }

    public static UpdateUserDTO updateUserDTO() {
        return new UpdateUserDTO(
                SARAH,
                null,
                null
        );
    }

    public static UserEntity updatedUserEntity() {
        return UserEntity.builder()
                .id(MOCK_USER_ID)
                .firstname(SARAH)
                .lastname(CONNOR)
                .email(JOHN_CONNOR_EMAIL)
                .role(Role.USER)
                .build();
    }

    public static UserDTO updatedUserDTO() {
        return new UserDTO(
                MOCK_USER_ID,
                SARAH,
                CONNOR,
                JOHN_CONNOR_EMAIL,
                Role.USER
        );
    }
}
