package com.demo.sercret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Profile({"default", "local", "k8s"}) // Active locally or if mounted in k8s via files
public class FileSecretProvider implements SecretProvider {

    @Value("${sftp.key:${user.home}/.ssh/id_rsa}")
    private String privateKeyPath;

    @Override
    public String getPrivateKey() {
        System.out.println("privateKeyPath :: "+ privateKeyPath);
        try {
            return Files.readString(Paths.get(privateKeyPath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read SSH private key from path: " + privateKeyPath, e);
        }
    }
}
