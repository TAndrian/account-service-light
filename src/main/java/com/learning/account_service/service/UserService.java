package com.learning.account_service.service;

import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UpdateUserDTO;
import com.learning.account_service.dto.UserDTO;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    /**
     * Retrieve all users.
     *
     * @return users.
     */
    Set<UserDTO> getUsers();

    /**
     * Retrieve specific user based on the given userId reference.
     *
     * @param userId user's id as reference.
     * @return user.
     */
    UserDTO getUserById(UUID userId);

    /**
     * Create user from the data given transferred with the userToCreateDTO.
     *
     * @param userToCreateDTO DTO which contains all user's data to create.
     * @return created user.
     */
    UserDTO createUser(CreateUserDTO userToCreateDTO);

    /**
     * Update user's data with the new one from the updateDTO.
     *
     * @param userId    target user's id.
     * @param updateDTO new user's data.
     * @return updated user.
     */
    UserDTO updateUser(UUID userId, UpdateUserDTO updateDTO);

    /**
     * Delete specific user based on the given userId reference.
     *
     * @param userId target user's id.
     * @return true if successful.
     */
    boolean deleteUser(UUID userId);
}
