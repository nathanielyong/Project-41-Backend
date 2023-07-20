package com.snowtheghost.redistributor.utils;

import com.snowtheghost.redistributor.configuration.EncryptionConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class EncryptionUtilsTest {

    @Mock
    private EncryptionConfiguration encryptionConfiguration;

    private EncryptionUtils encryptionUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(encryptionConfiguration.getBalanceEncryptionKey()).thenReturn(123);
        encryptionUtils = new EncryptionUtils(encryptionConfiguration);
    }

    @Test
    void testEncryptBalance() {
        int value = 500;
        String encryptedValue = encryptionUtils.encryptBalance(value);

        // Perform assertions on the encrypted value
        assertEquals("399", encryptedValue);
    }

    @Test
    void testDecryptBalance() {
        String encryptedValue = "399";
        int decryptedValue = encryptionUtils.decryptBalance(encryptedValue);

        // Perform assertions on the decrypted value
        assertEquals(500, decryptedValue);
    }

    @Test
    void testEncryptPassword() {
        String password = "password123";
        String encryptedPassword = encryptionUtils.encryptPassword(password);

        // Perform assertions on the encrypted password
        assertEquals("ab3fe4003f14e3ef573417f95e47d4985c482eadd139c08b3758eeae7cc60b9d", encryptedPassword);
    }
}
