package com.demo.logging;

import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Component
public class MessageLogger {

    private String channelName;
    private StringBuilder messageBuilder;


    public void log(Logger logger, Message<?> message, String channel) {
        if (logger.isInfoEnabled()) {
            this.messageBuilder = new StringBuilder();
            MessageHeaders headers = message.getHeaders();
            if (headers != null && !headers.isEmpty()) {
                this.messageBuilder.append("channel=").append("\"").append(channel).append("\"");
                headers.forEach((key, value) -> {
                    if (value != null) {
                        this.messageBuilder.append(",").append(key).append("=").append("\"").append(value).append("\"");
                    }
                });
                // Trim the trailing comma and space
                if (this.messageBuilder.toString().endsWith(", ")) {
                    this.messageBuilder.setLength(this.messageBuilder.length() - 2);
                }

            }
            logger.info(this.messageBuilder.toString());
        }
    }
}