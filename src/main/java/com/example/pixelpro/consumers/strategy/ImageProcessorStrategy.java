package com.example.pixelpro.consumers.strategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

public interface ImageProcessorStrategy {
    ByteArrayOutputStream process (byte[] data);

    static ByteArrayOutputStream convertToByteArrayOutputStream(Mat processedImage) {

        BytePointer buf = new BytePointer();
        opencv_imgcodecs.imencode(".jpg", processedImage, buf);

        byte[] bytes = new byte[(int) buf.limit()];
        buf.get(bytes);
        buf.deallocate(); 

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos;
    }
}
