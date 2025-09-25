package com.example.pixelpro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {
    
    @Value("${mini.url}")
    private String url;

    @Value("${mini.user}")
    private String user;
    
    @Value("${mini.passwd}")
    private String password;
    
    // @Value("${mini.bucket}")
    public static String bucket = "pixelpro";

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(user, password)
                .build();
    }
}
