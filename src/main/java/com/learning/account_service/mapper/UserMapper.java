package com.learning.account_service.mapper;

import com.learning.account_service.business.enums.UserEntity;
import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Set<UserDTO> toDTOs(List<UserEntity> userEntities);

    UserDTO toDTO(UserEntity userEntity);

    UserEntity fromCreateUserDTO(CreateUserDTO createUserDTO);
}
