package com.example.pixelpro.services;

import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.stereotype.Service;

@Service
public class ImageServices {

    

    public void grayscale (String imagePath) {

        Mat image = opencv_imgcodecs.imread(imagePath);

        Mat grayImage = new Mat();
        // opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_RGB2GRAY);
        opencv_core.bitwise_not(image, grayImage);

        opencv_imgcodecs.imwrite("edited_image.jpg", grayImage);

    }
}