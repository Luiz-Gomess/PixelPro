package com.example.pixelpro.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.pixelpro.model.JobListDTO;
import com.example.pixelpro.repository.JobRepository;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;

    public Page<JobListDTO> getJobs(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return jobRepository.findAll(pageable).map(JobListDTO::new);
    }
}
