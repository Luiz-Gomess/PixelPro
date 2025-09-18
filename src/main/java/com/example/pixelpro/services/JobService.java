package com.example.pixelpro.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pixelpro.model.Job;
import com.example.pixelpro.model.JobListDTO;
import com.example.pixelpro.model.JobPostDTO;
import com.example.pixelpro.repository.JobRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;

    public Page<JobListDTO> getJobs(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return jobRepository.findAll(pageable).map(JobListDTO::new);
    }

    public Job map(JobPostDTO jobData, MultipartFile image) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String jobDataAsSting = mapper.writeValueAsString(jobData);
        Job job = mapper.readValue(jobDataAsSting, Job.class);

        job.addOriginalImage(image);

        return job;
    }
}
