package com.example.pixelpro.workers.strategy;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.model.Job;

public class GrayscaleStrategy implements ImageProcessorStrategy{

    @Override
    public void process(Job job) {

        Mat image = opencv_imgcodecs.imread(new BytePointer(job.getOriginalImage()));
        Mat grayImage = new Mat();

        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_RGB2GRAY);
        opencv_imgcodecs.imwrite( job.getImageFilename(), grayImage);
    }
    
}
