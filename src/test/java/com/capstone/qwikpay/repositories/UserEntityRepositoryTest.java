package com.capstone.qwikpay.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.qwikpay.entities.UserEntity;

public class UserEntityRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testFindByUsernameWhenUserExists() {
        // Arrange
        UserEntity user = new UserEntity(1, "john_doe", "john@example.com", "password123", "1234567890", "123 Street", "Male", null);
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername("john_doe");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");

        // Verify
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    public void testFindByUsernameWhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistent_user")).thenReturn(Optional.empty());

        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername("nonexistent_user");

        // Assert
        assertThat(foundUser).isNotPresent();

        // Verify
        verify(userRepository, times(1)).findByUsername("nonexistent_user");
    }

    @Test
    public void testExistsByUsername() {
        // Arrange
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);

        // Act
        Boolean exists = userRepository.existsByUsername("john_doe");

        // Assert
        assertThat(exists).isTrue();

        // Verify
        verify(userRepository, times(1)).existsByUsername("john_doe");
    }

    @Test
    public void testExistsByEmail() {
        // Arrange
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        // Act
        Boolean exists = userRepository.existsByEmail("john@example.com");

        // Assert
        assertThat(exists).isTrue();

        // Verify
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }

    @Test
    public void testFindByEmailWhenUserExists() {
        // Arrange
        UserEntity user = new UserEntity(1, "john_doe", "john@example.com", "password123", "1234567890", "123 Street", "Male", null);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail("john@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");

        // Verify
        verify(userRepository, times(1)).findByEmail("john@example.com");
    }

    @Test
    public void testExistsByMobile() {
        // Arrange
        when(userRepository.existsByMobile("1234567890")).thenReturn(true);

        // Act
        Boolean exists = userRepository.existsByMobile("1234567890");

        // Assert
        assertThat(exists).isTrue();

        // Verify
        verify(userRepository, times(1)).existsByMobile("1234567890");
    }
}
