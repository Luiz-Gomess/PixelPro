package com.example.pixelpro.consumers.strategy.impl;

import java.io.ByteArrayOutputStream;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;

public class InvertColorsStrategy implements ImageProcessorStrategy {

    @Override
    public ByteArrayOutputStream process(byte[] data) {

        Mat image = opencv_imgcodecs.imdecode(new Mat(new BytePointer(data)), opencv_imgcodecs.IMREAD_COLOR);
        Mat invert = new Mat();

        // Invert colors
        opencv_core.bitwise_not(image, invert);

        return ImageProcessorStrategy.convertToByteArrayOutputStream(invert);
    }
}
