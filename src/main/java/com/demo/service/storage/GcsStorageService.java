package com.demo.service.storage;

import com.demo.service.StorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Profile("gcs")
@RequiredArgsConstructor
@Slf4j
public class GcsStorageService implements StorageService {

    private final Storage storage;

    @Value("${gcp.storage.bucket:one-step-bucket}")
    private String bucket;

    @Override
    public String save(String stage,
                     String fileName,
                     String payload) {

        String objectName = stage + "/" + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket, objectName)
                .setContentType("application/xml")
                .build();
        // 1. Capture the returned Blob object from the create method
        Blob blob = storage.create(blobInfo, payload.getBytes(StandardCharsets.UTF_8));
        // 2. Generate the path formats
        String gsPath = String.format("gs://%s/%s", blob.getBucket(), blob.getName());
        String httpsUrl = String.format("https://googleapis.com", blob.getBucket(), blob.getName());

//        System.out.println("GCS URI: " + gsPath);          // Output: gs://your-bucket/stage/filename.xml
//        System.out.println("HTTP URL: " + httpsUrl);       // Output: https://googleapis.com
//        storage.create(blobInfo,
//                payload.getBytes(StandardCharsets.UTF_8));
        return gsPath;
    }
}