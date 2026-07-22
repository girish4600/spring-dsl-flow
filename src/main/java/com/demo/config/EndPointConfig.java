package com.demo.config;

import com.demo.sercret.SecretProvider;
import com.demo.sftp.SftpSessionConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
public class EndPointConfig {

    @Value("${outbound.sftp}")
    private String uri;

    @Value("${sftp.key:~/.ssh/id_rsa")
    private String key;

    @Autowired
    private SecretProvider secretProvider;

    private SftpSessionConfiguration sftpSessionFactory(URI uri) {
        return new SftpSessionConfiguration(uri, this.secretProvider);
    }

    @Bean
    public CachingSessionFactory<?> cachingSessionFactory() {
        try {
            return new CachingSessionFactory<>(sftpSessionFactory(new URI(uri)));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    @Bean
    public MessageHandler sftpHandler(CachingSessionFactory<SftpClient.DirEntry> factory) {
        log.info(" {}", uri);
        SftpMessageHandler handler =
                new SftpMessageHandler(factory);

        handler.setFileNameGenerator(message ->
                (String) message.getHeaders().get(FileHeaders.FILENAME));
        try {
            handler.setRemoteDirectoryExpression(
                    new LiteralExpression(new URI(uri).getPath()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        handler.setAutoCreateDirectory(true);
        return handler;

    }

    private void createDirectory(org.springframework.integration.file.remote.session.Session<SftpClient.DirEntry> session, String path) throws IOException {
        String parent = new File(path).getParent();
        if (parent == null) {
            throw new IOException("No parent Directory");
        }
        if (!session.exists(parent)) {
            createDirectory(session, parent);
        }
        log.info("Creating Dir " + path);
        session.mkdir(path);
    }

    /*public MessageHandler sftpHandler(CachingSessionFactory<SftpClient.DirEntry> factory) {
        log.info("using Sftp {}", uri);
        *//*try {
            URI url = new URI(uri);
            CachingSessionFactory<?> factory = this.getcachingSessionFactory(this.sftpSessionFactory(url));
            try (org.springframework.integration.file.remote.session.Session<SftpClient.DirEntry> session = (org.springframework.integration.file.remote.session.Session<SftpClient.DirEntry>) factory.getSession()) {
                try {
                    session.list(url.getPath());
                } catch (IOException e) {
                    createDirectory(session, url.getPath());
                }
            } catch (IOException e) {
                log.info("Cannot create SFTP destination");
            }
            SftpMessageHandler handler = new SftpMessageHandler((SessionFactory<SftpClient.DirEntry>) factory);
            handler.setFileNameGenerator(message -> (String) message.getHeaders()
                    .get(FileHeaders.FILENAME));
//            handler.setRemoteDirectoryExpressionString("'upload'");

            return handler;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }*//*
    }*/

}
