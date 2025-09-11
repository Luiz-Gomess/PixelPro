package com.example.pixelpro.workers.strategy;

import com.example.pixelpro.model.Job;

public interface ImageProcessorStrategy {
    void process (Job job);
}
