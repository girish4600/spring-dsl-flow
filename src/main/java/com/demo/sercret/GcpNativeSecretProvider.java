package com.demo.sercret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("gcp") // Active when deployed in GCP environment
public class GcpNativeSecretProvider implements SecretProvider {

    // Spring Cloud GCP resolves "sm://" syntax automatically into the secret content
    @Value("${sm://sftp-private-key-secret}") 
    private String privateKeyContent;

    @Override
    public String getPrivateKey() {
        return privateKeyContent;
    }
}
