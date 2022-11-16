package com.core.backend.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class AzureBlobStorageConfig {

    @Value("${spring.cloud.azure.storage.blob.account-name}")
    private String accountName;

    @Value("${spring.cloud.azure.storage.blob.account-key}")
    private String accountKey;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    @Bean
    public BlobContainerClient getBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName))
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient()
                .getBlobContainerClient(containerName);
    }
}