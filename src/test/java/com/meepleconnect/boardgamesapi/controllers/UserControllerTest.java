package com.meepleconnect.boardgamesapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meepleconnect.boardgamesapi.models.User;
import com.meepleconnect.boardgamesapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void testRegisterUser_Failure() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userService.registerUser(any(User.class)))
            .thenThrow(new RuntimeException("Registratiefout"));

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Fout bij registratie: Registratiefout"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void testGetUserById_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
