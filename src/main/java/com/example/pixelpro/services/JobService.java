package com.example.pixelpro.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pixelpro.config.MinioConfig;
import com.example.pixelpro.model.Job;
import com.example.pixelpro.model.JobListDTO;
import com.example.pixelpro.model.JobPostDTO;
import com.example.pixelpro.repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class JobService {

    private final String rawImagesFolder = "raw";
    private final String processedImagesFolder = "processed";

    @Autowired
    MinioClient minioClient;
    
    @Autowired
    private JobRepository jobRepository;

    public Page<JobListDTO> getJobs(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return jobRepository.findAll(pageable).map(JobListDTO::new);
    }

    public Job map(JobPostDTO jobData, String imageIdOnMini, MultipartFile image) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jobDataAsSting = mapper.writeValueAsString(jobData);
        Job job = mapper.readValue(jobDataAsSting, Job.class);

        job.setImageIdOnMini(imageIdOnMini);
        job.setImageFilename(image.getOriginalFilename());

        return job;
    }

    public void uploadObject(String imageIdOnMini, String objectName, InputStream inputStream, long size, String contentType, boolean newInsertion) {
        try {

            // Defines the destination folder based on the param.
            String path = newInsertion == true ? rawImagesFolder : processedImagesFolder;

            // Uses the jobId to create a unique folder to it's processed image.
            path = Path.of(path, imageIdOnMini, objectName).toString();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(MinioConfig.bucket)
                            .object(path)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build());
                            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do arquivo para o MinIO: " + e.getMessage());
        }
    }

    public InputStream getObject(String imageIdOnMini, String objectName, boolean raw) {
        try {
            String path = Path.of(raw ? rawImagesFolder : processedImagesFolder, imageIdOnMini, objectName).toString();
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(MinioConfig.bucket)
                            .object(path)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao resgatar o arquivo do MinIO: " + e.getMessage());
        }
    }
}
