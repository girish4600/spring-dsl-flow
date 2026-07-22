package com.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface StorageService {
    String save(String stage, String fileName, String payload);
}
