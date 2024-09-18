package com.learning.account_service.controller.resource.v1.user;

import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UpdateUserDTO;
import com.learning.account_service.dto.UserDTO;
import com.learning.account_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

import static com.learning.account_service.controller.resource.v1.ResourcePath.BASE_URL;
import static com.learning.account_service.controller.resource.v1.ResourcePath.PATH_USER_ID;
import static com.learning.account_service.controller.resource.v1.ResourcePath.USER;
import static com.learning.account_service.controller.resource.v1.ResourcePath.USERS;

@RestController
@RequestMapping(
        path = BASE_URL,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Validated
public class UserResourceController {

    private final UserService userService;

    @GetMapping(USERS)
    @ResponseStatus(HttpStatus.OK)
    public Set<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(PATH_USER_ID)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping(USER)
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody CreateUserDTO userToCreateDTO) {
        return userService.createUser(userToCreateDTO);
    }

    @PutMapping(PATH_USER_ID)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDTO updateDTO) {
        return userService.updateUser(userId, updateDTO);
    }

    @DeleteMapping(PATH_USER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable UUID userId) {
        return userService.deleteUser(userId);
    }
}
