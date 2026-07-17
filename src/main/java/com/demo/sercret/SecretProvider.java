package com.demo.sercret;

public interface SecretProvider {
    /**
     * Retrieves the raw private key contents.
     * @return The SSH Private Key string.
     */
    String getPrivateKey();
}
