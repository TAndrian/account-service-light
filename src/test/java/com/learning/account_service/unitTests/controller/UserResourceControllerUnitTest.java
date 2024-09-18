package com.learning.account_service.unitTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.account_service.controller.resource.v1.user.UserResourceController;
import com.learning.account_service.exception.ConflictException;
import com.learning.account_service.exception.NotFoundException;
import com.learning.account_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.learning.account_service.controller.resource.v1.ResourcePath.BASE_URL;
import static com.learning.account_service.controller.resource.v1.ResourcePath.PATH_USER_ID;
import static com.learning.account_service.controller.resource.v1.ResourcePath.USER;
import static com.learning.account_service.controller.resource.v1.ResourcePath.USERS;
import static com.learning.account_service.util.UserMocks.MOCK_CREATE_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_INVALID_USER_TO_CREATE_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_NOT_FOUND_USER_ID;
import static com.learning.account_service.util.UserMocks.MOCK_UPDATED_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_UPDATE_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_USER_DTO;
import static com.learning.account_service.util.UserMocks.MOCK_USER_DTOs;
import static com.learning.account_service.util.UserMocks.MOCK_USER_ID;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserResourceController.class)
@AutoConfigureMockMvc
class UserResourceControllerUnitTest {

    private static final String API_URL = BASE_URL;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void when_getUsers_then_return_users() throws Exception {
        final String URL = API_URL.concat(USERS);

        when(userService.getUsers()).thenReturn(MOCK_USER_DTOs);

        String responseBody = objectMapper.writeValueAsString(MOCK_USER_DTOs);

        // ACT
        mockMvc.perform(get(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // ASSERT
        verify(userService, times(1)).getUsers();
    }

    @Test
    void given_userId_when_getUserById_then_return_user() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(PATH_USER_ID);

        when(userService.getUserById(MOCK_USER_ID)).thenReturn(MOCK_USER_DTO);

        String responseBody = objectMapper.writeValueAsString(MOCK_USER_DTO);

        // ACT
        mockMvc.perform(get(URL, MOCK_USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // ASSERT
        verify(userService, times(1)).getUserById(MOCK_USER_ID);
    }

    @Test
    void given_not_found_userId_when_getUserById_then_return_notFound_error() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(PATH_USER_ID);
        when(userService.getUserById(MOCK_NOT_FOUND_USER_ID))
                .thenThrow(NotFoundException.class);

        // ACT
        mockMvc.perform(get(URL, MOCK_NOT_FOUND_USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // ASSERT
        verify(userService, times(1)).getUserById(MOCK_NOT_FOUND_USER_ID);
    }

    @Test
    void given_createUserDTO_when_createUser_then_create_user() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(USER);

        when(userService.createUser(MOCK_CREATE_USER_DTO)).thenReturn(MOCK_USER_DTO);

        String requestBody = objectMapper.writeValueAsString(MOCK_CREATE_USER_DTO);
        String responseBody = objectMapper.writeValueAsString(MOCK_USER_DTO);

        // ACT
        mockMvc.perform(post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));

        // ASSERT
        verify(userService, times(1)).createUser(MOCK_CREATE_USER_DTO);
    }

    @Test
    void given_user_already_exists_when_createUser_then_return_conflict_error() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(USER);

        when(userService.createUser(MOCK_CREATE_USER_DTO)).thenThrow(ConflictException.class);

        String requestBody = objectMapper.writeValueAsString(MOCK_CREATE_USER_DTO);

        // ACT
        mockMvc.perform(post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());

        // ASSERT
        verify(userService, times(1)).createUser(MOCK_CREATE_USER_DTO);
    }

    @Test
    void given_invalid_createUserDTO_when_createUser_then_return_badRequest_error() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(USER);

        Set<String> expectedErrors = new HashSet<>();
        expectedErrors.add("Password is required.");
        expectedErrors.add("Firstname is required.");

        String requestBody = objectMapper.writeValueAsString(MOCK_INVALID_USER_TO_CREATE_DTO);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();

        // ASSERT

        /* Extract message values from mvcResult */
        String message = extractValueResponse(mvcResult);
        Set<String> actualErrors = new HashSet<>(Arrays.asList(message.split("; ")));

        assertAll(
                () -> verify(userService, times(0))
                        .createUser(MOCK_INVALID_USER_TO_CREATE_DTO),
                () -> assertEquals(expectedErrors, actualErrors)
        );
    }

    @Test
    void given_updateUserDTO_when_updateUser_then_update_user() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(PATH_USER_ID);
        when(userService.updateUser(MOCK_USER_ID, MOCK_UPDATE_USER_DTO))
                .thenReturn(MOCK_UPDATED_USER_DTO);

        String requestBody = objectMapper.writeValueAsString(MOCK_UPDATE_USER_DTO);
        String responseBody = objectMapper.writeValueAsString(MOCK_UPDATED_USER_DTO);

        // ACT
        mockMvc.perform(put(URL, MOCK_USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));

        // ASSERT
        verify(userService, times(1)).updateUser(MOCK_USER_ID, MOCK_UPDATE_USER_DTO);
    }

    @Test
    void given_userId_when_deleteUser_then_delete_user() throws Exception {
        // ARRANGE
        final String URL = API_URL.concat(PATH_USER_ID);
        when(userService.deleteUser(MOCK_USER_ID))
                .thenReturn(true);

        // ACT
        mockMvc.perform(delete(URL, MOCK_USER_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // ASSERT
        verify(userService, times(1)).deleteUser(MOCK_USER_ID);
    }

    /**
     * Extract value from message field in mvcResult.
     *
     * @param mvcResult response.
     * @return Extract value from message field.
     * @throws UnsupportedEncodingException exception.
     * @throws JsonProcessingException      exception.
     */
    private String extractValueResponse(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String responseBody = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("message").asText();
    }
}
