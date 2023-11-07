package com.snowtheghost.project41.services;

import com.snowtheghost.project41.database.repositories.UserRepository;
import com.snowtheghost.project41.utils.EncryptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    // @Test
    // void testGetUser_ValidId() {
    //     String userId = "userId";
    //     User user = new User(userId, "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");

    //     when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

    //     User result = userService.getUser(userId);

    //     assertEquals(user, result);
    // }

    // @Test
    // void testGetUser_InvalidId() {
    //     String userId = "invalidUserId";

    //     when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

    //     assertThrows(EntityNotFoundException.class, () -> userService.getUser(userId));
    // }

    // @Test
    // void testHasSufficientFunds_SufficientBalance() {
    //     User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     int amount = 50;

    //     when(encryptionUtils.decryptBalance(user.getEncryptedBalance())).thenReturn(100);

    //     boolean result = userService.hasSufficientFunds(user, amount);

    //     assertTrue(result);
    // }

    // @Test
    // void testHasSufficientFunds_InsufficientBalance() {
    //     User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     int amount = 200;

    //     when(encryptionUtils.decryptBalance(user.getEncryptedBalance())).thenReturn(100);

    //     boolean result = userService.hasSufficientFunds(user, amount);

    //     assertFalse(result);
    // }

    // @Test
    // void testGetUserByEmail_ValidEmail() {
    //     String email = "email";
    //     User user = new User("userId", "username", email, "encryptedPassword", "encryptedBalance", "connectedAccountId");

    //     when(userRepository.findByEmail(email)).thenReturn(user);

    //     User result = userService.getUserByEmail(email);

    //     assertEquals(user, result);
    // }

    // @Test
    // void testGetUserByEmail_InvalidEmail() {
    //     String email = "invalidEmail";

    //     when(userRepository.findByEmail(email)).thenReturn(null);

    //     User result = userService.getUserByEmail(email);

    //     assertNull(result);
    // }

    // @Test
    // void testIsValidCredentials_ValidCredentials() {
    //     User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     String email = "email";
    //     String password = "password";

    //     when(encryptionUtils.encryptPassword(password)).thenReturn(user.getEncryptedPassword());

    //     boolean result = userService.isValidCredentials(user, email, password);

    //     assertTrue(result);
    // }

    // @Test
    // void testIsValidCredentials_InvalidCredentials() {
    //     User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     String email = "email";
    //     String password = "invalidPassword";

    //     when(encryptionUtils.encryptPassword(password)).thenReturn("invalidEncryptedPassword");

    //     boolean result = userService.isValidCredentials(user, email, password);

    //     assertFalse(result);
    // }

    // @Test
    // void testAddFunds() {
    //     String userId = "userId";
    //     User user = new User(userId, "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     int amount = 50;
    //     String updatedEncryptedBalance = "updatedEncryptedBalance";

    //     when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    //     when(encryptionUtils.decryptBalance(user.getEncryptedBalance())).thenReturn(100);
    //     when(encryptionUtils.encryptBalance(150)).thenReturn(updatedEncryptedBalance);
    //     when(userRepository.save(user)).thenReturn(user);

    //     userService.addFunds(userId, amount);

    //     verify(userRepository).save(user);
    //     assertEquals(updatedEncryptedBalance, user.getEncryptedBalance());
    // }

    // @Test
    // void testSubtractFunds() {
    //     User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     int amount = 50;
    //     String updatedEncryptedBalance = "updatedEncryptedBalance";

    //     when(encryptionUtils.decryptBalance(user.getEncryptedBalance())).thenReturn(100);
    //     when(encryptionUtils.encryptBalance(50)).thenReturn(updatedEncryptedBalance);
    //     when(userRepository.save(user)).thenReturn(user);

    //     userService.subtractFunds(user, amount);

    //     verify(userRepository).save(user);
    //     assertEquals(updatedEncryptedBalance, user.getEncryptedBalance());
    // }

    // @Test
    // void testGetBalance() {
    //     User user = new User("userId", "username", "email", "encryptedPassword", "encryptedBalance", "connectedAccountId");
    //     String encryptedBalance = "encryptedBalance";
    //     float decryptedBalance = 100.0f;

    //     when(encryptionUtils.decryptBalance(user.getEncryptedBalance())).thenReturn((int) (decryptedBalance * 100));

    //     float result = userService.getBalance(user);

    //     assertEquals(decryptedBalance, result);
    // }
}
