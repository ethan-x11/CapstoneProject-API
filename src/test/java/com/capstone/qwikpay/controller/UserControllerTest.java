package com.capstone.qwikpay.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capstone.qwikpay.entities.UserEntity;
import com.capstone.qwikpay.exceptions.UserNotFoundException;
import com.capstone.qwikpay.services.UserService;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    public UserControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser_Success() throws UserNotFoundException {
        Integer userId = 1;
        UserEntity user = new UserEntity();
        when(userService.getUser(userId)).thenReturn(user);

        UserEntity response = userController.getUser(userId);

        assertNotNull(response);
        assertEquals(user, response);
    }

    @Test
    void testGetUser_NotFound() throws UserNotFoundException {
        Integer userId = 1;

        when(userService.getUser(userId)).thenThrow(new UserNotFoundException("User not found"));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userController.getUser(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdateUser() throws UserNotFoundException {
        UserEntity user = new UserEntity();
        when(userService.updateUser(user)).thenReturn(user);

        UserEntity response = userController.updateUser(user);

        assertNotNull(response);
        assertEquals(user, response);
    }

    @Test
    void testDeleteUser() throws UserNotFoundException {
        Integer userId = 1;
        String successMessage = "User deleted successfully";

        when(userService.deleteUser(userId)).thenReturn(successMessage);

        String response = userController.deleteUser(userId);

        assertNotNull(response);
        assertEquals(successMessage, response);
    }

    @Test
    void testGetUsers() {
        List<UserEntity> users = Arrays.asList(new UserEntity(), new UserEntity());
        when(userService.getUsers()).thenReturn(users);

        List<UserEntity> response = userController.getUsers();

        assertNotNull(response);
        assertEquals(users, response);
    }
}
