package com.learning.account_service.mapper;

import com.learning.account_service.business.enums.UserEntity;
import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UpdateUserDTO;
import com.learning.account_service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    /**
     * Convert List of UserEntity into Set of UserDTO.
     *
     * @param userEntities List of UserEntity.
     * @return Set of UserDTO.
     */
    Set<UserDTO> toDTOs(List<UserEntity> userEntities);

    /**
     * Convert UserEntity into UserDTO.
     *
     * @param userEntity UserEntity to convert.
     * @return UserDTO.
     */
    UserDTO toDTO(UserEntity userEntity);

    /**
     * Convert CreateUserDTO into UserEntity.
     *
     * @param createUserDTO to convert.
     * @return UserEntity.
     */
    UserEntity fromCreateUserDTO(CreateUserDTO createUserDTO);

    /**
     * Update UserEntity fields with UpdateUserDTO fields.
     *
     * @param updateDTO  to update with userEntity.
     * @param userEntity target entity.
     */
    void updateUserFromDTO(UpdateUserDTO updateDTO, @MappingTarget UserEntity userEntity);
}
