package com.demo.service.storage;

import com.demo.service.StorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Profile("local")
@Slf4j
public class FileStorageService implements StorageService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String save(String folder, String name, String payload) {

        try {
            File dir = new File(folder);
            boolean isDirCreated = dir.mkdirs();
            if(isDirCreated) {
                File file = new File(dir, name);
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, payload);
                return file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folder;
    }

}