package com.meepleconnect.boardgamesapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Path uploadDir = Paths.get("uploads");

    @BeforeEach
    void setUp() throws Exception {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void uploadFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.matchesPattern("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.txt$")));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void uploadFile_EmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "empty.txt",
            "text/plain",
            new byte[0]
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File is empty"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void uploadFile_Unauthorized() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void uploadAndDownloadFile_Success() throws Exception {
        // Upload een bestand
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            "text/plain",
            "Hello, World!".getBytes()
        );

        String filename = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                .file(file))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Download het bestand
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/download/" + filename))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + filename + "\""))
                .andExpect(content().bytes("Hello, World!".getBytes()));
    }

    @Test
    void downloadFile_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/download/nonexistent.txt"))
                .andExpect(status().isNotFound());
    }
} 