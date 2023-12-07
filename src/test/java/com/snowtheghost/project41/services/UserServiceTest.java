package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.models.User;
import com.snowtheghost.project41.database.repositories.UserRepository;
import com.snowtheghost.project41.utils.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionUtils encryptionUtils;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        String userId = "userId";
        String username = "username";
        String email = "email";
        String password = "password";
        String encryptedPassword = "encryptedPassword";
        String encryptedBalance = "encryptedBalance";

        when(encryptionUtils.encryptPassword(password)).thenReturn(encryptedPassword);
        when(encryptionUtils.encryptBalance(0)).thenReturn(encryptedBalance);

        userService.createPlayerUser(userId, username, email, password);

        verify(userRepository).save(any());
    }

    @Test
    void testDeleteUser() {
        String userId = "userId";
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        verify(userRepository).save(any());
    }
}
