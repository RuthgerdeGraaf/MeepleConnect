//package com.meepleconnect.boardgamesapi.controllers;
//
//import com.meepleconnect.boardgamesapi.models.User;
//import com.meepleconnect.boardgamesapi.services.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SuppressWarnings("unused")
//class UserControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testRegisterUser_Success() {
//        User user = new User();
//        user.setUsername("TestUser");
//        user.setPassword("password");
//
//        when(userService.registerUser(user)).thenReturn(user);
//
//        ResponseEntity<?> response = userController.registerUser(user);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//        verify(userService, times(1)).registerUser(user);
//    }
//
//    @Test
//    void testRegisterUser_Failure() {
//        User user = new User();
//        user.setUsername("TestUser");
//
//        when(userService.registerUser(user)).thenThrow(new RuntimeException("Registratiefout"));
//
//        ResponseEntity<?> response = userController.registerUser(user);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Fout bij registratie: Registratiefout", response.getBody());
//        verify(userService, times(1)).registerUser(user);
//    }
//
//    @Test
//    @WithMockUser(roles = {"EMPLOYEE"})
//    void testGetUserById_Success() {
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("TestUser");
//
//        when(userService.getUserById(1L)).thenReturn(user);
//
//        ResponseEntity<Object> response = userController.getUserById(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//    }
//
//    @Test
//    @WithMockUser(roles = {"EMPLOYEE"})
//    void testDeleteUser_Success() {
//        doNothing().when(userService).deleteUser(1L);
//
//        ResponseEntity<Void> response = userController.deleteUser(1L);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(userService, times(1)).deleteUser(1L);
//    }
//}
