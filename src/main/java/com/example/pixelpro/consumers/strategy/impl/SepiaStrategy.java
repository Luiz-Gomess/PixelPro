package com.example.pixelpro.consumers.strategy.impl;

import java.io.ByteArrayOutputStream;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;

public class SepiaStrategy implements ImageProcessorStrategy{

    @Override
    public ByteArrayOutputStream process(byte[] data) {

        Mat image = new Mat(new BytePointer(data));
        image = opencv_imgcodecs.imdecode(image, opencv_imgcodecs.IMREAD_COLOR);

        float[] sepiaData = new float[]{
            0.272f, 0.534f, 0.131f,
            0.349f, 0.686f, 0.168f,
            0.393f, 0.769f, 0.189f
        };

        FloatPointer fp = new FloatPointer(sepiaData);

        Mat sepiaKernel = new Mat(3, 3, opencv_core.CV_32F, fp);

        Mat sepia = new Mat();
        opencv_core.transform(image, sepia, sepiaKernel);

       return ImageProcessorStrategy.convertToByteArrayOutputStream(sepia);
    }    
}
