package com.snowtheghost.redistributor.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EncryptionConfiguration {

    @Value("${encryption.balance.key}")
    private int balanceEncryptionKey;
}
