package com.demo.config;

import com.demo.model.Message;
import com.demo.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileHeaders;

@Configuration
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
                .nullChannel();
    }
}