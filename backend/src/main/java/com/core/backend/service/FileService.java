package com.core.backend.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Boolean uploadFile(MultipartFile file);
    ByteArrayResource downloadFile(String fileName);
    Boolean deleteFile(String filename);
}
