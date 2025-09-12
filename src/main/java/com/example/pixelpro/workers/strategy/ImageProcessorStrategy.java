package com.example.pixelpro.workers.strategy;

import java.io.ByteArrayOutputStream;

import com.example.pixelpro.model.Job;

public interface ImageProcessorStrategy {
    ByteArrayOutputStream process (Job job);
}
