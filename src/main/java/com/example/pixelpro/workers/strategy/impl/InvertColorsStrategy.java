package com.example.pixelpro.workers.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.model.Job;
import com.example.pixelpro.workers.strategy.ImageProcessorStrategy;

public class InvertColorsStrategy implements ImageProcessorStrategy {

    @Override
    public ByteArrayOutputStream process(Job job) {

        byte[] imageBytes = job.getOriginalImage();

        // Converter os bytes em Mat usando imdecode
        Mat image = opencv_imgcodecs.imdecode(new Mat(new BytePointer(imageBytes)), opencv_imgcodecs.IMREAD_COLOR);
        Mat invert = new Mat();

        // Invert colors
        opencv_core.bitwise_not(image, invert);
        opencv_imgcodecs.imwrite( job.getImageFilename(), invert);

        BytePointer buf = new BytePointer();
        opencv_imgcodecs.imencode(".jpg", invert, buf);

        // Converter para ByteArrayOutputStream
        byte[] bytes = new byte[(int) buf.limit()];
        buf.get(bytes);
        buf.deallocate(); // libera memória nativa

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(bytes);
            // job.setImageResult(baos.toByteArray());
            // jobsRepository.save(job);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos;
    }
    
}
