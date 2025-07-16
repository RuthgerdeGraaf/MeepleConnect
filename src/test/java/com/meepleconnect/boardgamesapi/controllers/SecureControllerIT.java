package com.meepleconnect.boardgamesapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class SecureControllerIT {

        @Autowired
        private WebApplicationContext webApplicationContext;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders
                                .webAppContextSetup(webApplicationContext)
                                .apply(org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
                                                .springSecurity())
                                .build();
        }

        @Test
        @WithMockUser
        void getSecureData_WithAuthenticatedUser_ShouldReturnSecureData() throws Exception {
                mockMvc.perform(get("/secure"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string("This is secure data: "));
        }

        @Test
        void getSecureData_WithoutAuthentication_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure"))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getAdminData_WithAdminRole_ShouldReturnAdminData() throws Exception {
                mockMvc.perform(get("/secure/admin"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string("This is secure admin data: "));
        }

        @Test
        @WithMockUser(roles = "USER")
        void getAdminData_WithUserRole_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure/admin"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getAdminData_WithoutAuthentication_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure/admin"))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "USER")
        void getUserData_WithUserRole_ShouldReturnUserData() throws Exception {
                mockMvc.perform(get("/secure/user"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string("This is secure user data: "));
        }

        @Test
        @WithMockUser(roles = "ADMIN")
        void getUserData_WithAdminRole_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure/user"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getUserData_WithoutAuthentication_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure/user"))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        void getSecureData_WithEmployeeRole_ShouldReturnSecureData() throws Exception {
                mockMvc.perform(get("/secure"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                                .andExpect(content().string("This is secure data: "));
        }

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        void getAdminData_WithEmployeeRole_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure/admin"))
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = "EMPLOYEE")
        void getUserData_WithEmployeeRole_ShouldReturnForbidden() throws Exception {
                mockMvc.perform(get("/secure/user"))
                                .andExpect(status().isForbidden());
        }
}