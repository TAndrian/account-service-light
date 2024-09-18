package com.learning.account_service.unitTests.service;

import com.learning.account_service.dto.UserDTO;
import com.learning.account_service.exception.ConflictException;
import com.learning.account_service.exception.NotFoundException;
import com.learning.account_service.exception.enums.v1.UserExceptionEnums;
import com.learning.account_service.mapper.UserMapper;
import com.learning.account_service.repository.UserRepository;
import com.learning.account_service.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static com.learning.account_service.util.UserMocks.MOCK_CREATE_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_NOT_FOUND_USER_ID;
import static com.learning.account_service.util.UserMocks.MOCK_UPDATED_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_UPDATED_USER_ENTITY;
import static com.learning.account_service.util.UserMocks.MOCK_UPDATE_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_USER_DTOs;
import static com.learning.account_service.util.UserMocks.MOCK_USER_ENTITIES;
import static com.learning.account_service.util.UserMocks.MOCK_USER_ENTITY;
import static com.learning.account_service.util.UserMocks.MOCK_USER_ID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @Test
    void when_getUsers_then_return_users() {
        // ARRANGE
        when(userRepository.findAll())
                .thenReturn(MOCK_USER_ENTITIES);
        when(userMapper.toDTOs(MOCK_USER_ENTITIES))
                .thenReturn(MOCK_USER_DTOs);

        // ACT
        Set<UserDTO> expected = userService.getUsers();

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findAll(),
                () -> verify(userMapper, times(1)).toDTOs(MOCK_USER_ENTITIES),
                () -> assertEquals(MOCK_USER_DTOs, expected)
        );
    }

    @Test
    void given_userId_when_getUserById_then_return_user() {
        // ARRANGE
        when(userRepository.findById(MOCK_USER_ID))
                .thenReturn(Optional.ofNullable(MOCK_USER_ENTITY));
        when(userMapper.toDTO(MOCK_USER_ENTITY))
                .thenReturn(MOCK_USER_DTO);

        // ACT
        UserDTO expected = userService.getUserById(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(MOCK_USER_ID),
                () -> verify(userMapper, times(1)).toDTO(MOCK_USER_ENTITY),
                () -> assertEquals(MOCK_USER_DTO, expected)
        );
    }

    @Test
    void given_not_found_userId_when_getUserById_then_return_notFound_error() {

        // ACT
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(MOCK_NOT_FOUND_USER_ID)
        );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(MOCK_NOT_FOUND_USER_ID),
                () -> verify(userMapper, times(0)).toDTO(any()),
                () -> assertEquals(UserExceptionEnums.USER_NOT_FOUND.getValue(), exception.getMessage())
        );
    }

    @Test
    void given_createUserDTO_when_createUser_then_create_user() {
        // ARRANGE
        when(userMapper.fromCreateUserDTO(MOCK_CREATE_USER_DTO))
                .thenReturn(MOCK_USER_ENTITY);
        when(userRepository.save(MOCK_USER_ENTITY))
                .thenReturn(MOCK_USER_ENTITY);
        when(userMapper.toDTO(MOCK_USER_ENTITY))
                .thenReturn(MOCK_USER_DTO);

        // ACT
        UserDTO expected = userService.createUser(MOCK_CREATE_USER_DTO);

        // ASSERT
        assertAll(
                () -> verify(userMapper, times(1)).fromCreateUserDTO(MOCK_CREATE_USER_DTO),
                () -> verify(userRepository, times(1)).save(MOCK_USER_ENTITY),
                () -> verify(userMapper, times(1)).toDTO(MOCK_USER_ENTITY),
                () -> assertEquals(MOCK_USER_DTO, expected)
        );
    }

    @Test
    void given_user_already_exists_when_createUser_then_return_conflict_error() {
        // ARRANGE
        when(userRepository.findByEmailIgnoreCase(MOCK_CREATE_USER_DTO.email()))
                .thenReturn(Optional.ofNullable(MOCK_USER_ENTITY));

        // ACT
        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> userService.createUser(MOCK_CREATE_USER_DTO)
        );

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1))
                        .findByEmailIgnoreCase(MOCK_USER_DTO.email()),
                () -> verify(userMapper, times(0)).fromCreateUserDTO(any()),
                () -> verify(userRepository, times(0)).save(any()),
                () -> verify(userMapper, times(0)).toDTO(any()),
                () -> assertEquals(UserExceptionEnums.USER_ALREADY_EXISTS.getValue(), exception.getMessage())
        );
    }

    @Test
    void given_updateUserDTO_when_updateUser_then_update_user() {
        // ARRANGE
        when(userRepository.findById(MOCK_USER_ID))
                .thenReturn(Optional.ofNullable(MOCK_USER_ENTITY));

        doNothing().when(userMapper)
                .updateUserFromDTO(MOCK_UPDATE_USER_DTO, MOCK_USER_ENTITY);

        assert MOCK_USER_ENTITY != null;
        when(userRepository.save(MOCK_USER_ENTITY))
                .thenReturn(MOCK_UPDATED_USER_ENTITY);

        when(userMapper.toDTO(MOCK_UPDATED_USER_ENTITY))
                .thenReturn(MOCK_UPDATED_USER_DTO);
        // ACT
        UserDTO expected = userService.updateUser(MOCK_USER_ID, MOCK_UPDATE_USER_DTO);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(MOCK_USER_ID),
                () -> verify(userMapper, times(1))
                        .updateUserFromDTO(MOCK_UPDATE_USER_DTO, MOCK_USER_ENTITY),
                () -> verify(userRepository, times(1)).save(MOCK_USER_ENTITY),
                () -> verify(userMapper, times(1)).toDTO(MOCK_UPDATED_USER_ENTITY),
                () -> assertEquals(MOCK_UPDATED_USER_DTO, expected)
        );
    }

    @Test
    void given_userId_when_deleteUser_then_delete_user() {
        // ARRANGE
        when(userRepository.findById(MOCK_USER_ID))
                .thenReturn(Optional.ofNullable(MOCK_USER_ENTITY));

        assert MOCK_USER_ENTITY != null;
        doNothing().when(userRepository).delete(MOCK_USER_ENTITY);

        // ACT
        boolean expected = userService.deleteUser(MOCK_USER_ID);

        // ASSERT
        assertAll(
                () -> verify(userRepository, times(1)).findById(MOCK_USER_ID),
                () -> verify(userRepository, times(1)).delete(MOCK_USER_ENTITY),
                () -> assertTrue(expected)
        );
    }
}
