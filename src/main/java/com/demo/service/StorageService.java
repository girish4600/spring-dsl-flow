package com.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface StorageService {
    void save(String stage,
              String fileName,
              String payload);
}
