package com.example.pixelpro.consumers.strategy.impl;

import java.io.ByteArrayOutputStream;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;

public class BlurStrategy implements ImageProcessorStrategy{

    @Override
    public ByteArrayOutputStream process(byte[] data) {
        Mat image = new Mat(new BytePointer(data));
        image = opencv_imgcodecs.imdecode(image, opencv_imgcodecs.IMREAD_COLOR);

        Mat blurred = new Mat();
        opencv_imgproc.GaussianBlur(image, blurred, new Size(15, 15), 0);

        return ImageProcessorStrategy.convertToByteArrayOutputStream(blurred);

    
    }

    
}
