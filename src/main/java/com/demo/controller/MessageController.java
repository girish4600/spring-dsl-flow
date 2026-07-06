package com.demo.controller;

import com.demo.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@Slf4j
public class MessageController {

    @Autowired
    private DirectChannel inputChannel;



    @PostMapping(
            value = "/messages",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(
            @RequestParam MultipartFile file) throws IOException {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                inputChannel.send(
                        MessageBuilder.withPayload(file.getBytes())
                                .setHeader(FileHeaders.FILENAME, file.getOriginalFilename())
                                .setHeader("contentType", file.getContentType())
                                .setHeader("fileSize", file.getSize())
                                .setHeader("receivedTimestamp", timestamp)
                                .build());

        return ResponseEntity.ok("Accepted");

    }

}