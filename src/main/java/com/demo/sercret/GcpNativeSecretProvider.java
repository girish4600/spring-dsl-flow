package com.demo.sercret;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("gcs")
@Slf4j // Active when deployed in GCP environment
public class GcpNativeSecretProvider implements SecretProvider {

    // Spring Cloud GCP resolves "sm://" syntax automatically into the secret content
    @Value("${sm://sftp-private-key}")
    private String privateKeyContent;

    @Override
    public String getPrivateKey() {
        log.info("using gcp secrets");
        return privateKeyContent;
    }
}
