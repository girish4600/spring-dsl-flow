package com.demo.sftp;

import com.demo.sercret.SecretProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
public class SftpSessionConfiguration extends DefaultSftpSessionFactory implements SessionFactory<SftpClient.DirEntry>{

    private URI uri;

    public SftpSessionConfiguration(URI uri, SecretProvider secretProvider){
        this.uri=uri;
        setHost(uri.getHost());
        int port = uri.getPort() != -1 ? uri.getPort(): 22;
        setPort(port);
        setUser(uri.getUserInfo());
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String passwordName = parameters.getFirst("passwordName");
        if(passwordName != null){
            setPassword("password");//get password from secrets
        }
        String privateKey = parameters.getFirst("privateKey");
        setPrivateKey(new ByteArrayResource(secretProvider.getPrivateKey()
                .getBytes(StandardCharsets.UTF_8)));
       /* if(privateKey == null && passwordName == null){
            privateKey = "id_rsa";
        }

        if (privateKey != null && !privateKey.trim().isEmpty()) {
            // 1. Check if the string is raw key content instead of a path
            if (privateKey.contains("-----BEGIN") || privateKey.contains("ssh-rsa")) {
                // Secret Manager returned the raw private key contents
                setPrivateKey(new ByteArrayResource(privateKey.getBytes(StandardCharsets.UTF_8)));
            } else {
                // Treat it as a file path (Local or Kubernetes Secret Mount)
                try {
                    Path keyPath = Paths.get(privateKey.trim());
                    if (Files.exists(keyPath)) {
                        setPrivateKey(new FileSystemResource(keyPath));
                    } else {
                        throw new IllegalArgumentException("Private key file path specified but does not exist: " + privateKey);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid private key path or key format provided", e);
                }
            }
        }*/

       /* if(privateKey!=null){
            setPrivateKey(new ByteArrayResource("secrete--from gke".getBytes()));
        }*/
        setAllowUnknownKeys(true);

        setTimeout(1000 * 60 *10);
//        this.setSshClientConfigurer(sshClient -> new SftpSSHClient);
//        setPrivateKey(new FileSystemResource("/etc/ssh/id_rsa"));// this is from k8s
    }
}