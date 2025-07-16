package com.meepleconnect.boardgamesapi.services;

import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.exceptions.UserNotFoundException;
import com.meepleconnect.boardgamesapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Role userRole;
    private Role adminRole;
    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleName("USER");

        adminRole = new Role();
        adminRole.setRoleName("ADMIN");

        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password123");
        testUser.setRoles(List.of(userRole));
        testUser.setEnabled(true);
        testUser.setExpired(false);
        testUser.setLocked(false);
        testUser.setAreCredentialsExpired(false);

        testUser2 = new User();
        testUser2.setUserName("testuser2");
        testUser2.setPassword("password456");
        testUser2.setRoles(List.of(adminRole));
        testUser2.setEnabled(true);
        testUser2.setExpired(false);
        testUser2.setLocked(false);
        testUser2.setAreCredentialsExpired(false);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> expectedUsers = List.of(testUser, testUser2);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testUser, testUser2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void registerUser_ShouldEncodePasswordAndSaveUser() {
        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.registerUser(testUser);

        assertThat(result).isEqualTo(testUser);
        assertThat(testUser.getPassword()).isEqualTo(encodedPassword);
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void getUserById_WithValidId_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertThat(result).isEqualTo(testUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WithInvalidId_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with ID 999 not found.");

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void updateUser_WithPasswordChange_ShouldEncodeNewPassword() {
        User userDetails = new User();
        userDetails.setUserName("updateduser");
        userDetails.setPassword("newpassword");
        userDetails.setRoles(List.of(adminRole));

        String encodedNewPassword = "encodedNewPassword";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn(encodedNewPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, userDetails);

        assertThat(result).isEqualTo(testUser);
        assertThat(testUser.getUserName()).isEqualTo("updateduser");
        assertThat(testUser.getPassword()).isEqualTo(encodedNewPassword);
        assertThat(testUser.getRoles()).isEqualTo(List.of(adminRole));
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_WithoutPasswordChange_ShouldNotEncodePassword() {
        User userDetails = new User();
        userDetails.setUserName("updateduser");
        userDetails.setPassword(null);
        userDetails.setRoles(List.of(adminRole));

        String originalPassword = testUser.getPassword();
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, userDetails);

        assertThat(result).isEqualTo(testUser);
        assertThat(testUser.getUserName()).isEqualTo("updateduser");
        assertThat(testUser.getPassword()).isEqualTo(originalPassword);
        assertThat(testUser.getRoles()).isEqualTo(List.of(adminRole));
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_WithEmptyPassword_ShouldNotEncodePassword() {
        User userDetails = new User();
        userDetails.setUserName("updateduser");
        userDetails.setPassword("");
        userDetails.setRoles(List.of(adminRole));

        String originalPassword = testUser.getPassword();
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, userDetails);

        assertThat(result).isEqualTo(testUser);
        assertThat(testUser.getUserName()).isEqualTo("updateduser");
        assertThat(testUser.getPassword()).isEqualTo(originalPassword);
        assertThat(testUser.getRoles()).isEqualTo(List.of(adminRole));
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_WithInvalidId_ShouldThrowUserNotFoundException() {
        User userDetails = new User();
        userDetails.setUserName("updateduser");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(999L, userDetails))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with ID 999 not found.");

        verify(userRepository, times(1)).findById(999L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldCallDeleteById() {
        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTotalUsersCount_ShouldReturnTotalCount() {
        when(userRepository.count()).thenReturn(10L);

        long result = userService.getTotalUsersCount();

        assertThat(result).isEqualTo(10L);
        verify(userRepository, times(1)).count();
    }
}