package com.demo.global.config;

import com.demo.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.integration.file.FileHeaders;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@GlobalChannelInterceptor(patterns = "*")
public class LoggingInterceptor implements ChannelInterceptor {

    @Autowired
    private StorageService storage;

    @Override
    public Message<?> preSend(Message<?> message,
                              MessageChannel channel12) {
        String channel = ((DirectChannel) channel12).getBeanName().toString();
        String loggingId =
                message.getHeaders().get("loggingId", String.class);
        Object originalId =
                message.getHeaders().get("originalId", Object.class);
        String timestamp = message.getHeaders()
                .get("receivedTimestamp", String.class);

        String originalFileName = message.getHeaders()
                .get(FileHeaders.FILENAME, String.class);

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));

        String fileName = baseName + "_" + timestamp + extension;
        if( loggingId != null && originalId != null) {
            log.info("""
                channel="{}", loggingId="{}", originalId="{}""",
                    channel,
                    loggingId,
                    originalId);
            String payload = message.getHeaders().get("payload", String.class);

            Object payload1 = message.getPayload();

            String content;

            if (payload1 instanceof byte[] bytes) {
                content = new String(bytes, StandardCharsets.UTF_8);
            } else {
                content = String.valueOf(payload);
            }

//            storage.save(stage, content);
//            storage.save(payload, content);
            storage.save(payload, fileName, content);
        }

        return message;
    }
}