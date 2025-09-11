package com.example.pixelpro.workers.strategy.impl;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.model.Job;
import com.example.pixelpro.workers.strategy.ImageProcessorStrategy;

public class InvertColorsStrategy implements ImageProcessorStrategy {

    @Override
    public void process(Job job) {

        byte[] imageBytes = job.getOriginalImage();

        // Converter os bytes em Mat usando imdecode
        Mat image = opencv_imgcodecs.imdecode(new Mat(new BytePointer(imageBytes)), opencv_imgcodecs.IMREAD_COLOR);
        Mat grayImage = new Mat();

        // Invert colors
        opencv_core.bitwise_not(image, grayImage);
        opencv_imgcodecs.imwrite( job.getImageFilename(), grayImage);
    }
    
}
