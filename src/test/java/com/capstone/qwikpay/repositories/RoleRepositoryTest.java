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

import com.capstone.qwikpay.entities.Role;
import com.capstone.qwikpay.enums.ERole;

public class RoleRepositoryTest {

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testFindByNameWhenRoleExists() {
        // Arrange
        Role adminRole = new Role(1, ERole.ROLE_ADMIN);
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));

        // Act
        Optional<Role> foundRole = roleRepository.findByName(ERole.ROLE_ADMIN);

        // Assert
        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getName()).isEqualTo(ERole.ROLE_ADMIN);
        assertThat(foundRole.get().getId()).isEqualTo(1);

        // Verify
        verify(roleRepository, times(1)).findByName(ERole.ROLE_ADMIN);
    }

    @Test
    public void testFindByNameWhenRoleDoesNotExist() {
        // Arrange
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.empty());

        // Act
        Optional<Role> foundRole = roleRepository.findByName(ERole.ROLE_USER);

        // Assert
        assertThat(foundRole).isNotPresent();

        // Verify
        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
    }
}
