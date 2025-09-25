package com.example.pixelpro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;

@Component
public class MinioBucketInitializer implements CommandLineRunner {

    private final String bucketName = MinioConfig.bucket;
    private static final Logger log = LoggerFactory.getLogger(MinioConfig.class);

    @Autowired
    private MinioClient minioClient;

    @Override
    public void run(String... args) throws Exception { 
        log.info("Verifyng the existence of the bucket");

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Bucket '{}' created successfully!", bucketName);
        } else {
            log.info("Bucket '{}' already exists.", bucketName);
        }
    }
}