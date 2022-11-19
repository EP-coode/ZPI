package com.core.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Boolean uploadFile(MultipartFile file, String newFileName);
    byte[] downloadFile(String fileName);
    Boolean deleteFile(String filename);
}
