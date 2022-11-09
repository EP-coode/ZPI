package com.core.backend.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;

@Log4j2
@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private BlobContainerClient blobContainerClient;

    @Override
    public Boolean uploadFile(MultipartFile file) {
        boolean isSuccess = true;
        String filename = file.getOriginalFilename();
        try {
            BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(filename).getBlockBlobClient();
            if (blockBlobClient.exists()) {
                blockBlobClient.delete();
            }
            blockBlobClient.upload(new BufferedInputStream(file.getInputStream()), file.getSize(), true);
        } catch (IOException e) {
            isSuccess = false;
            log.error("Error while processing file {}", e.getLocalizedMessage());
        }
        return isSuccess;
    }

    @Override
    public ByteArrayResource downloadFile(String fileName) {
        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(fileName).getBlockBlobClient();
        return new ByteArrayResource(blockBlobClient.downloadContent().toBytes());
    }

    @Override
    public Boolean deleteFile(String fileName) {
        BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient(fileName).getBlockBlobClient();
        if (blockBlobClient.exists()) {
            blockBlobClient.delete();
            return true;
        }
        return false;
    }
}
