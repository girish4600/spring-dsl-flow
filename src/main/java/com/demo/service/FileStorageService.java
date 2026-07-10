package com.demo.service;

import com.demo.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class FileStorageService implements StorageService {

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(String folder, String name, String payload) {

        try {
            File dir = new File("payloads/" + folder);
            dir.mkdirs();
            File file = new File(dir,
                    name);
            log.info("fileRef="+ "\""+file.getAbsolutePath() +"\"");
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, payload);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}