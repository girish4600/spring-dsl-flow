package com.demo.config;

import org.apache.sshd.sftp.client.SftpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

@Configuration
//@Import(SftpConfig.class)
public class IntegrationFlowConfig {

    private static final Logger log = LoggerFactory.getLogger(IntegrationFlowConfig.class);
    private static final String FILENAME = "";


    @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow messageFlow() {

        return IntegrationFlow.from(inputChannel())
                 .enrichHeaders(h -> h
                        .headerExpression("originalId",
                                String.valueOf(Math.random()))
                )
                .channel("channel.message.from.ups")
                .enrichHeaders(h -> h
                        .header("payload", "upstream", true)
                        .headerFunction("loggingId",
                                message -> message.getHeaders().get(FileHeaders.FILENAME))
                )
                .channel("channel.header.enrichment")
                .enrichHeaders(h -> h.header("payload", "enriched", true))
                // Transformation
                .channel("channel.transformation")
                .enrichHeaders(h -> h.header("payload", "downstream", true))
                .channel("channel.delivery")
                .handle(sftpHandler(cachingSessionFactory(
                        sftpSessionFactory()))).get();
//                .nullChannel();
    }

    @Bean
    public DefaultSftpSessionFactory sftpSessionFactory() {

        DefaultSftpSessionFactory factory =
                new DefaultSftpSessionFactory(true);
        factory.setAllowUnknownKeys(true);
        factory.setHost("sftp-service");

        factory.setPort(22);

        factory.setUser("demo");

//        factory.setPassword("password");
        factory.setPrivateKey(
                new FileSystemResource("/etc/ssh/id_rsa")
        );
        return factory;
    }

    @Bean
    public CachingSessionFactory<?> cachingSessionFactory(
            DefaultSftpSessionFactory factory) {

        return new CachingSessionFactory<>(factory);
    }

    @Bean
    public MessageHandler sftpHandler(
            CachingSessionFactory<?> factory) {

        SftpMessageHandler handler =
                new SftpMessageHandler((SessionFactory<SftpClient.DirEntry>) factory);
        handler.setFileNameGenerator(message ->
                (String) message.getHeaders().get(FileHeaders.FILENAME));
        handler.setRemoteDirectoryExpressionString("'upload'");
//        handler.setRemoteDirectoryExpression(new LiteralExpression("."));
        return handler;

    }
}