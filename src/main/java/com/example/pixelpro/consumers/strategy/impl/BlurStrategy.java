package com.example.pixelpro.consumers.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Size;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;
import com.example.pixelpro.model.Job;

public class BlurStrategy implements ImageProcessorStrategy{

    @Override
    public ByteArrayOutputStream process(Job job) {
        Mat image = new Mat(new BytePointer(job.getOriginalImage()));
        image = opencv_imgcodecs.imdecode(image, opencv_imgcodecs.IMREAD_COLOR);

        Mat blurred = new Mat();

        // Aplicar Gaussian Blur (kernel 15x15)
        opencv_imgproc.GaussianBlur(image, blurred, new Size(15, 15), 0);

        BytePointer buf = new BytePointer();
        opencv_imgcodecs.imencode(".jpg", blurred, buf);

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
