package com.example.pixelpro.consumers.strategy;

import java.io.ByteArrayOutputStream;

import com.example.pixelpro.model.Job;

public interface ImageProcessorStrategy {
    ByteArrayOutputStream process (Job job);
}
