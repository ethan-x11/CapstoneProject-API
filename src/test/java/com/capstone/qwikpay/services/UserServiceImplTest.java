package com.capstone.qwikpay.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;
import com.capstone.qwikpay.repositories.UserRepository;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addNewUser_ShouldReturnSavedUser() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserEntity savedUser = userService.addNewUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUser_WhenUserExists_ShouldReturnUser() throws UserNotFoundException {
        // Arrange
        Integer userId = 1;
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUsername("testuser");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserEntity foundUser = userService.getUser(userId);

        // Assert
        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getUser(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws UserNotFoundException {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setUsername("updateduser");

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserEntity updatedUser = userService.updateUser(user);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("updateduser", updatedUser.getUsername());
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setId(1);

        when(userRepository.existsById(user.getId())).thenReturn(false);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, never()).save(user);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnSuccessMessage() throws UserNotFoundException {
        // Arrange
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        String message = userService.deleteUser(userId);

        // Assert
        assertEquals("User with ID 1 deleted successfully", message);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowException() {
        // Arrange
        Integer userId = 1;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void getUsers_ShouldReturnListOfUsers() {
        // Arrange
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserEntity> users = userService.getUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }
}
