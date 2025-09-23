package com.example.pixelpro.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;

@Configuration
public class MinioConfig {
    
    @Value("${mini.url}")
    private String url;

    @Value("${mini.user}")
    private String user;
    
    @Value("${mini.passwd}")
    private String password;
    
    @Value("${mini.bucket}")
    public static String bucket;

    private static final Logger log = LoggerFactory.getLogger(MinioConfig.class);

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(user, password)
                .build();
    }

    @PostConstruct
    public void ensureBucketExists() throws Exception{
        MinioClient client = minioClient();
        boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            log.info("Bucket created!");
        } else {
            log.info("Bucket " + bucket + "alredy exists.");
        }
    }
}
