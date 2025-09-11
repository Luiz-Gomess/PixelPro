package com.example.pixelpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pixelpro.model.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
}
