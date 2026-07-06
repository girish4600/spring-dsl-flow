package com.demo.service;

import com.demo.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileStorageService {

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(String folder, String name, String payload) {

        try {
            File dir = new File("payloads/" + folder);
            dir.mkdirs();
            File file = new File(dir,
                    name);
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}