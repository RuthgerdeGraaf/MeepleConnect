package com.meepleconnect.boardgamesapi.controllers;

import com.meepleconnect.boardgamesapi.exceptions.BadRequestException;
import com.meepleconnect.boardgamesapi.exceptions.FileNotFoundException;
import com.meepleconnect.boardgamesapi.exceptions.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileControllerIT {

        @Autowired
        private WebApplicationContext webApplicationContext;

        private MockMvc mockMvc;
        private final Path uploadPath = Paths.get("uploads");

        @BeforeAll
        void setupAll() throws IOException {
                if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                }
        }

        @BeforeEach
        void setUp() throws IOException {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
                if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                }
                FileUtils.cleanDirectory(uploadPath.toFile());
        }

        @Test
        void constructor_ShouldCreateUploadDirectoryIfNotExists() throws Exception {
                if (Files.exists(uploadPath)) {
                        FileUtils.deleteDirectory(uploadPath.toFile());
                }

                assertThat(Files.exists(uploadPath)).isFalse();

                FileController fileController = new FileController();

                assertThat(Files.exists(uploadPath)).isTrue();
                assertThat(Files.isDirectory(uploadPath)).isTrue();
        }

        @Test
        void constructor_IOExceptionScenario_ShouldThrowRuntimeException() throws Exception {
                String invalidPathName = "a".repeat(300); // Path too long
                Path invalidPath = Paths.get(invalidPathName);

                try (MockedStatic<Paths> pathsMock = Mockito.mockStatic(Paths.class);
                                MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {

                        pathsMock.when(() -> Paths.get("uploads")).thenReturn(invalidPath);
                        filesMock.when(() -> Files.exists(invalidPath)).thenReturn(false);
                        filesMock.when(() -> Files.createDirectories(invalidPath))
                                        .thenThrow(new IOException("Path name too long"));

                        assertThatThrownBy(() -> new FileController())
                                        .isInstanceOf(RuntimeException.class)
                                        .hasMessage("Could not create upload directory")
                                        .hasCauseInstanceOf(IOException.class);
                }
        }

        @Test
        void uploadFile_IOExceptionDuringCopy_ShouldThrowFileUploadException() throws Exception {
                MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
                                "Test content".getBytes());

                try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
                        filesMock.when(() -> Files.exists(Mockito.any(Path.class))).thenCallRealMethod();
                        filesMock.when(() -> Files.createDirectories(Mockito.any(Path.class))).thenCallRealMethod();

                        filesMock.when(() -> Files.copy(Mockito.any(java.io.InputStream.class),
                                        Mockito.any(Path.class)))
                                        .thenThrow(new IOException("Disk full"));

                        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload").file(file))
                                        .andExpect(status().isInternalServerError())
                                        .andExpect(jsonPath("$.error").value("File Upload Error"))
                                        .andExpect(jsonPath("$.message").value("Could not upload file: Disk full"));
                }
        }

        @Test
        void uploadFile_ShouldStoreFileAndReturnFilename() throws Exception {
                MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE,
                                "Hello World".getBytes());

                String response = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                                .file(file))
                                .andExpect(status().isOk())
                                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                                .andReturn().getResponse().getContentAsString();

                assertThat(response).endsWith(".txt");
                Path storedFile = uploadPath.resolve(response);
                assertThat(Files.exists(storedFile)).isTrue();
                assertThat(Files.readString(storedFile)).isEqualTo("Hello World");
        }

        @Test
        void uploadFile_WithEmptyFile_ShouldReturnBadRequest() throws Exception {
                MockMultipartFile file = new MockMultipartFile("file", "empty.txt", MediaType.TEXT_PLAIN_VALUE,
                                new byte[0]);

                mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                                .file(file))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(jsonPath("$.message").value("File is empty"));
        }

        @Test
        void downloadFile_ShouldReturnFileContent() throws Exception {
                String content = "Download test";
                MockMultipartFile file = new MockMultipartFile("file", "download.txt", MediaType.TEXT_PLAIN_VALUE,
                                content.getBytes());
                String filename = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                                .file(file))
                                .andReturn().getResponse().getContentAsString();

                mockMvc.perform(get("/api/files/" + filename))
                                .andExpect(status().isOk())
                                .andExpect(header().string("Content-Disposition",
                                                "attachment; filename=\"" + filename + "\""))
                                .andExpect(content().bytes(content.getBytes()));
        }

        @Test
        void downloadFile_FileNotFound_ShouldReturnNotFound() throws Exception {
                mockMvc.perform(get("/api/files/nonexistent.txt"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("File Not Found"));
        }

        @Test
        void downloadFile_InvalidUrl_ShouldReturnNotFound() throws Exception {
                mockMvc.perform(get("/api/files/invalid%2Ffile.txt"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("File Not Found"));
        }

        @Test
        void uploadFile_ShouldCreateUniqueFilenames() throws Exception {
                MockMultipartFile file1 = new MockMultipartFile("file", "a.txt", MediaType.TEXT_PLAIN_VALUE,
                                "A".getBytes());
                MockMultipartFile file2 = new MockMultipartFile("file", "b.txt", MediaType.TEXT_PLAIN_VALUE,
                                "B".getBytes());

                String filename1 = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload").file(file1))
                                .andReturn().getResponse().getContentAsString();
                String filename2 = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload").file(file2))
                                .andReturn().getResponse().getContentAsString();

                assertThat(filename1).isNotEqualTo(filename2);
                assertThat(Files.readString(uploadPath.resolve(filename1))).isEqualTo("A");
                assertThat(Files.readString(uploadPath.resolve(filename2))).isEqualTo("B");
        }

        @Test
        void downloadFile_ShouldReturnOctetStreamContentType() throws Exception {
                MockMultipartFile file = new MockMultipartFile("file", "octet.txt", MediaType.TEXT_PLAIN_VALUE,
                                "octet".getBytes());
                String filename = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload").file(file))
                                .andReturn().getResponse().getContentAsString();

                mockMvc.perform(get("/api/files/" + filename))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        }

        @Test
        void downloadFile_FileExistsAsDirectoryNotReadable_ShouldReturnNotFound() throws Exception {
                String filename = "directory-not-file.txt";
                Path directoryPath = uploadPath.resolve(filename);

                Files.createDirectory(directoryPath);

                assertThat(Files.exists(directoryPath)).isTrue();
                assertThat(Files.isDirectory(directoryPath)).isTrue();

                mockMvc.perform(get("/api/files/" + filename))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("File Not Found"))
                                .andExpect(jsonPath("$.message").value("File '" + filename + "' not found"));

                Files.delete(directoryPath);
        }

        @AfterAll
        void cleanup() throws IOException {
                FileUtils.cleanDirectory(uploadPath.toFile());
        }
}