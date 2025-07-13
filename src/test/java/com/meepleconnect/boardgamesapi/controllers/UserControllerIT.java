package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.dtos.UserRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class UserControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAllUsers_ShouldReturnUsersList() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getAllUsers_ShouldReturnValidUserStructure() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].userName").exists())
                .andExpect(jsonPath("$[*].roles").exists());
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUser() throws Exception {
        UserRequestDTO newUser = createTestUser("getuser", "password123");
        String userJson = objectMapper.writeValueAsString(newUser);

        // User aanmaken
        String response = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Haal het id uit de response
        Long userId = objectMapper.readTree(response).get("id").asLong();

        // User ophalen
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.userName").value("getuser"))
                .andExpect(jsonPath("$.roles").exists());
    }

    @Test
    void getUserById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/users/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_WithValidUser_ShouldReturnCreatedUser() throws Exception {
        String uniqueUsername = "testuser_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "password123");
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userName").value(uniqueUsername))
                .andExpect(jsonPath("$.roles").exists())
                .andExpect(header().string("Location", containsString("/api/users/")));
    }

    @Test
    void registerUser_WithEmptyUserName_ShouldReturnBadRequest() throws Exception {
        UserRequestDTO newUser = createTestUser("", "password123");
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_WithNullUserName_ShouldReturnBadRequest() throws Exception {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setPassword("password123");
        newUser.setRoles(new String[] { "USER" });
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_WithEmptyPassword_ShouldReturnBadRequest() throws Exception {
        String uniqueUsername = "emptypass_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "");
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_WithNullPassword_ShouldReturnBadRequest() throws Exception {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setUsername("nullpass_" + System.currentTimeMillis());
        newUser.setRoles(new String[] { "USER" });
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_WithInvalidJson_ShouldReturnServerError() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateUser_WithValidUser_ShouldReturnUpdatedUser() throws Exception {
        // Eerst een user aanmaken
        String uniqueUsername = "updateuser_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "password123");
        String newUserJson = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();

        // ID ophalen uit de response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        Long userId = jsonNode.get("id").asLong();

        // Nu de user updaten
        UserRequestDTO updateUser = createTestUser("updateduser", "newpassword123");
        String userJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.userName").value("updateduser"))
                .andExpect(jsonPath("$.roles").exists());
    }

    @Test
    void updateUser_WithInvalidId_ShouldReturnNotFound() throws Exception {
        UserRequestDTO updateUser = createTestUser("updateduser", "newpassword123");
        String userJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/api/users/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        UserRequestDTO updateUser = createTestUser("updateduser", "newpassword123");
        String userJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/api/users/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithEmptyUserName_ShouldReturnBadRequest() throws Exception {
        // Eerst een user aanmaken
        String uniqueUsername = "emptyupdate_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "password123");
        String newUserJson = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();

        // ID ophalen uit de response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        Long userId = jsonNode.get("id").asLong();

        // Nu proberen te updaten met lege username
        UserRequestDTO updateUser = createTestUser("", "newpassword123");
        String userJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithNullUserName_ShouldReturnBadRequest() throws Exception {
        // Eerst een user aanmaken
        String uniqueUsername = "nullupdate_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "password123");
        String newUserJson = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();

        // ID ophalen uit de response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        Long userId = jsonNode.get("id").asLong();

        // Nu proberen te updaten met null username
        UserRequestDTO updateUser = new UserRequestDTO();
        updateUser.setPassword("newpassword123");
        updateUser.setRoles(new String[] { "USER" });
        String userJson = objectMapper.writeValueAsString(updateUser);

        mockMvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithInvalidJson_ShouldReturnServerError() throws Exception {
        // Eerst een user aanmaken
        String uniqueUsername = "invalidjson_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "password123");
        String newUserJson = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();

        // ID ophalen uit de response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        Long userId = jsonNode.get("id").asLong();

        // Nu proberen te updaten met invalid JSON
        String invalidJson = "{ invalid json }";

        mockMvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteUser_WithValidId_ShouldReturnNoContent() throws Exception {
        // Eerst een user aanmaken
        String uniqueUsername = "deleteuser_" + System.currentTimeMillis();
        UserRequestDTO newUser = createTestUser(uniqueUsername, "password123");
        String newUserJson = objectMapper.writeValueAsString(newUser);

        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();

        // ID ophalen uit de response
        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        Long userId = jsonNode.get("id").asLong();

        // Nu de user verwijderen
        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_WithInvalidId_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_WithNonNumericId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/users/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_WithAdminRole_ShouldReturnCreatedUser() throws Exception {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setUsername("adminuser");
        newUser.setPassword("adminpass123");
        newUser.setRoles(new String[] { "ADMIN" });
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("adminuser"))
                .andExpect(jsonPath("$.roles").exists());
    }

    @Test
    void registerUser_WithMultipleRoles_ShouldReturnCreatedUser() throws Exception {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setUsername("multiuser");
        newUser.setPassword("multipass123");
        newUser.setRoles(new String[] { "USER", "ADMIN" });
        String userJson = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("multiuser"))
                .andExpect(jsonPath("$.roles").exists());
    }

    private UserRequestDTO createTestUser(String userName, String password) {
        UserRequestDTO user = new UserRequestDTO();
        user.setUsername(userName);
        user.setPassword(password);
        user.setRoles(new String[] { "USER" });
        return user;
    }
}