package com.example.pixelpro.workers.strategy.impl;

import java.io.ByteArrayOutputStream;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import com.example.pixelpro.model.Job;
import com.example.pixelpro.workers.strategy.ImageProcessorStrategy;

public class BlurStrategy implements ImageProcessorStrategy{

    @Override
    public void process(Job job) {
        Mat image = new Mat(new BytePointer(job.getOriginalImage()));
        image = opencv_imgcodecs.imdecode(image, opencv_imgcodecs.IMREAD_COLOR);

        Mat blurred = new Mat();

        // Aplicar Gaussian Blur (kernel 15x15)
        opencv_imgproc.GaussianBlur(image, blurred, new Size(15, 15), 0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        opencv_imgcodecs.imwrite(job.getImageFilename(), blurred);

    
    }

    
}
