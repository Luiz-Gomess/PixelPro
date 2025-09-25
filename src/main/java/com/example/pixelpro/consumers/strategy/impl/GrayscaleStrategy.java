package com.example.pixelpro.consumers.strategy.impl;

import java.io.ByteArrayOutputStream;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;

public class GrayscaleStrategy implements ImageProcessorStrategy{

    @Override
    public ByteArrayOutputStream process(byte[] data) {

        Mat image = opencv_imgcodecs.imdecode(new Mat(new BytePointer(data)), opencv_imgcodecs.IMREAD_COLOR);

        Mat grayImage = new Mat();

        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.COLOR_BGR2GRAY);

        return ImageProcessorStrategy.convertToByteArrayOutputStream(grayImage);
    }
}
