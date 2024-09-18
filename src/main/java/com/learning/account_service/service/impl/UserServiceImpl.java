package com.learning.account_service.service.impl;

import com.learning.account_service.business.enums.UserEntity;
import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UpdateUserDTO;
import com.learning.account_service.dto.UserDTO;
import com.learning.account_service.exception.ConflictException;
import com.learning.account_service.exception.NotFoundException;
import com.learning.account_service.exception.enums.v1.UserExceptionEnums;
import com.learning.account_service.mapper.UserMapper;
import com.learning.account_service.repository.UserRepository;
import com.learning.account_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found with the userId: {}";
    public static final String USER_ALREADY_EXISTS_ERROR_MESSAGE = "User already exists with the userEmail: {}";
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public Set<UserDTO> getUsers() {
        return userMapper.toDTOs(userRepository.findAll());
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        return userMapper.toDTO(findUserById(userId));
    }

    @Override
    public UserDTO createUser(CreateUserDTO userToCreateDTO) {
        findUserByEmail(userToCreateDTO.email());
        UserEntity userToCreateEntity = userMapper.fromCreateUserDTO(userToCreateDTO);
        UserEntity createdUserEntity = userRepository.save(userToCreateEntity);
        return userMapper.toDTO(createdUserEntity);
    }

    @Override
    public UserDTO updateUser(UUID userId, UpdateUserDTO updateDTO) {
        UserEntity targetUserToUpdateEntity = findUserById(userId);
        userMapper.updateUserFromDTO(updateDTO, targetUserToUpdateEntity);
        return userMapper.toDTO(userRepository.save(targetUserToUpdateEntity));
    }

    @Override
    public boolean deleteUser(UUID userId) {
        UserEntity userToDeleteEntity = findUserById(userId);
        userRepository.delete(userToDeleteEntity);
        return true;
    }

    /**
     * Get user referenced by userId.
     *
     * @param userId reference.
     * @return user.
     */
    UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info(USER_NOT_FOUND_ERROR_MESSAGE, userId);
                    return new NotFoundException(
                            UserExceptionEnums.USER_NOT_FOUND.getValue(),
                            UserExceptionEnums.USER_EXCEPTION_CODE.getValue()
                    );
                });
    }

    /**
     * Find user referenced by email.
     *
     * @param userEmail reference.
     */
    void findUserByEmail(String userEmail) {
        boolean exists = userRepository.findByEmailIgnoreCase(userEmail).isPresent();
        if (exists) {
            log.info(USER_ALREADY_EXISTS_ERROR_MESSAGE, userEmail);
            throw new ConflictException(
                    UserExceptionEnums.USER_ALREADY_EXISTS.getValue(),
                    UserExceptionEnums.USER_EXCEPTION_CODE.getValue()
            );
        }
    }
}
