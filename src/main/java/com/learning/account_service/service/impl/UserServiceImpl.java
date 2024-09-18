package com.learning.account_service.service.impl;

import com.learning.account_service.business.enums.UserEntity;
import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UpdateUserDTO;
import com.learning.account_service.dto.UserDTO;
import com.learning.account_service.repository.UserRepository;
import com.learning.account_service.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public Set<UserDTO> getUsers() {
        return null;
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        return null;
    }

    @Override
    public UserDTO createUser(CreateUserDTO userToCreateDTO) {
        return null;
    }

    @Override
    public UserDTO updateUser(UUID userId, UpdateUserDTO updateDTO) {
        return null;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        return false;
    }

    /**
     * Get user referenced by userId.
     *
     * @param userId reference.
     * @return user.
     */
    UserEntity findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow();
    }

    /**
     * Find user referenced by email.
     *
     * @param userEmail reference.
     * @return user.
     */
    UserEntity findUserByEmail(String userEmail) {
        return userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow();
    }
}
