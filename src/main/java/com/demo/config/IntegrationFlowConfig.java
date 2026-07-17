package com.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.MessageHandler;

@Configuration
public class IntegrationFlowConfig {

    @Bean
    public DirectChannel inputChannel() {
        return new DirectChannel();
    }

    @Autowired
    public MessageHandler sftpHandler;

    @Bean
    public IntegrationFlow messageFlow() {

        return IntegrationFlow.from(inputChannel())
                 .enrichHeaders(h -> h.headerExpression("originalId",String.valueOf(Math.random())))
                .channel("channel.message.from.ups")
                .enrichHeaders(h -> h.header("payload", "upstream", true)
                        .headerFunction("loggingId", message -> message.getHeaders().get(FileHeaders.FILENAME)))
                .channel("channel.header.enrichment")
                .enrichHeaders(h -> h.header("payload", "enriched", true))
                // Transformation
                .channel("channel.transformation")
                .enrichHeaders(h -> h.header("payload", "downstream", true))
                .channel("channel.delivery.to.sftp")
                .handle(sftpHandler).get();
//                .nullChannel();
    }

}