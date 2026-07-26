package com.demo.global.config;

import com.demo.logging.MessageLogger;
import com.demo.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.integration.file.FileHeaders;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@GlobalChannelInterceptor(patterns = "*")
public class LoggingInterceptor implements ChannelInterceptor {

    @Autowired
    private StorageService storage;

    @Autowired
    private MessageLogger messageLogger;

    private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel12) {
        String channel = ((DirectChannel) channel12).getBeanName().toString();
        String loggingId = message.getHeaders().get("loggingId", String.class);
        Object originalId = message.getHeaders().get("originalId", Object.class);
        String timestamp = message.getHeaders().get("receivedTimestamp", String.class);

        String originalFileName = message.getHeaders().get(FileHeaders.FILENAME, String.class);

        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));

        String fileName = baseName + "_" + timestamp + extension;
        if (loggingId != null && originalId != null) {
            String payload = message.getHeaders().get("payload", String.class);
            Object payload1 = message.getPayload();
            String content;
            if (payload1 instanceof byte[] bytes) {
                content = new String(bytes, StandardCharsets.UTF_8);
            } else {
                content = String.valueOf(payload);
            }
            String fileRef = storage.save(payload, fileName, content);
            message = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders()).setHeader("fileRef", fileRef).build();
            messageLogger.log(this.logger, message, channel);
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        String logChannel = ((DirectChannel) channel).getBeanName();
        if (logChannel.contains("sftp")) {
            message = MessageBuilder.withPayload(message.getPayload()).copyHeaders(message.getHeaders()).setHeader("sftpComplete", true).build();
            messageLogger.log(this.logger, message, logChannel);
        }
    }
}