package com.learning.account_service.integrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.account_service.business.enums.UserEntity;
import com.learning.account_service.dto.CreateUserDTO;
import com.learning.account_service.dto.UserDTO;
import com.learning.account_service.exception.NotFoundException;
import com.learning.account_service.exception.enums.v1.UserExceptionEnums;
import com.learning.account_service.mapper.UserMapper;
import com.learning.account_service.repository.UserRepository;
import com.learning.account_service.util.UserMocks;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

import static com.learning.account_service.controller.resource.v1.ResourcePath.BASE_URL;
import static com.learning.account_service.controller.resource.v1.ResourcePath.PATH_USER_ID;
import static com.learning.account_service.controller.resource.v1.ResourcePath.USER;
import static com.learning.account_service.controller.resource.v1.ResourcePath.USERS;
import static com.learning.account_service.util.UserMocks.SARAH_CONNOR_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class UserResourceIntegrationTest {

    private static UUID JOHN_CONNOR_ID;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @LocalServerPort
    private Integer port;

    private String baseUrl = "http://localhost";

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("account-db-test")
            .withUsername("postgres")
            .withPassword("123456");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void init() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void setUp() {
        baseUrl = baseUrl + ":" + port + BASE_URL;
        populateDb();
    }

    @AfterAll
    static void tearDown() {
        postgreSQLContainer.close();
    }

    @AfterEach
    void cleanUp() {
        clearDB();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_get_all_users() throws Exception {
        // ARRANGE
        final String URL = baseUrl + USERS;

        Set<UserDTO> userDTOs = userMapper.toDTOs(userRepository.findAll());
        String expected = objectMapper.writeValueAsString(userDTOs);

        // ACT ASSERT
        mockMvc.perform(get(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void should_find_user_by_id() throws Exception {
        // ARRANGE
        final String URL = baseUrl + PATH_USER_ID;

        UserDTO userDTO = getExpectedUserDTOById(JOHN_CONNOR_ID);

        String expected = objectMapper.writeValueAsString(userDTO);

        // ACT ASSERT
        mockMvc.perform(get(URL, JOHN_CONNOR_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void should_create_user() throws Exception {
        // ARRANGE
        final String URL = baseUrl + USER;
        CreateUserDTO createSarahConnorDTO = UserMocks.createSarahConnorDTO();
        String requestBody = objectMapper.writeValueAsString(createSarahConnorDTO);

        // ACT
        MvcResult mvcResult = mockMvc.perform(post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        UserDTO expected = getResponseBody(mvcResult);
        UserEntity sarahConnorEntity = userRepository.findByEmailIgnoreCase(SARAH_CONNOR_EMAIL)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + SARAH_CONNOR_EMAIL));
        UserDTO sarahConnorDTO = userMapper.toDTO(sarahConnorEntity);

        // ASSERT
        assertEquals(sarahConnorDTO, expected);
    }

    @Test
    void should_update_user() throws Exception {
        // ARRANGE
        final String URL = baseUrl + PATH_USER_ID;
        String requestBody = objectMapper.writeValueAsString(UserMocks.updateUserDTO());

        // ACT
        MvcResult mvcResult = mockMvc.perform(put(URL, JOHN_CONNOR_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        UserDTO expected = getResponseBody(mvcResult);
        UserDTO johnConnorDTO = getExpectedUserDTOById(JOHN_CONNOR_ID);

        // ASSERT
        assertEquals(johnConnorDTO, expected);
    }

    @Test
    void should_delete_user() throws Exception {
        // ARRANGE
        final String URL = baseUrl + PATH_USER_ID;

        // ACT
        mockMvc.perform(delete(URL, JOHN_CONNOR_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        UserEntity johnConnorEntity = userRepository.findById(JOHN_CONNOR_ID).orElse(null);

        // ASSERT
        assertNull(johnConnorEntity);
    }

    /**
     * Get userDTO by userId.
     *
     * @return userDTO.
     */
    private UserDTO getExpectedUserDTOById(UUID userId) {
        return userMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        UserExceptionEnums.USER_NOT_FOUND.getValue(),
                        UserExceptionEnums.USER_EXCEPTION_CODE.getValue())));
    }

    /**
     * Return response body as DTO.
     *
     * @param mvcResult mvc result
     * @return Converted response body in UserDTO format.
     * @throws UnsupportedEncodingException exception.
     * @throws JsonProcessingException      exception.
     */
    private UserDTO getResponseBody(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, UserDTO.class);
    }

    /**
     * Populate DB.
     */
    private void populateDb() {
        UserEntity johnConnorEntity = UserMocks.johnConnorEntity();
        userRepository.save(johnConnorEntity);
        JOHN_CONNOR_ID = johnConnorEntity.getId();
    }

    /**
     * clear DB.
     */
    private void clearDB() {
        userRepository.deleteAll();
    }
}
