package com.demo.sercret;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("gcs")
@Slf4j
public class GcpManualSecretProvider implements SecretProvider {

    @Value("${gcp.project-id:one-step-gcp}")
    private String projectId;

    @Value("${gcp.secret-name:sftp-private-key}")
    private String secretName;

    @Override
    public String getPrivateKey() {
        log.info("Getting private key from Manual");
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretName, "latest");
            log.info("secretVersionName: {}",secretVersionName);
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            return response.getPayload().getData().toStringUtf8();
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch private key from GCP Secret Manager", e);
        }
    }
}
