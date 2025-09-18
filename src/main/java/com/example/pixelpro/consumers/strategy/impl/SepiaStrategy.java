package com.example.pixelpro.consumers.strategy.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;
import com.example.pixelpro.model.Job;

public class SepiaStrategy implements ImageProcessorStrategy{

    // @Autowired
    // private JobRepository jobsRepository;

    @Override
    public ByteArrayOutputStream process(Job job) {

        Mat image = new Mat(new BytePointer(job.getOriginalImage()));
        image = opencv_imgcodecs.imdecode(image, opencv_imgcodecs.IMREAD_COLOR);

        float[] sepiaData = new float[]{
            0.272f, 0.534f, 0.131f,
            0.349f, 0.686f, 0.168f,
            0.393f, 0.769f, 0.189f
        };

        // Criar Mat para imagem final
        FloatPointer fp = new FloatPointer(sepiaData);

        // Criar Mat 3x3 tipo CV_32F e copiar dados
        Mat sepiaKernel = new Mat(3, 3, opencv_core.CV_32F, fp);

        Mat sepia = new Mat();
        opencv_core.transform(image, sepia, sepiaKernel);

        // Usar BytePointer como buffer para imencode
        BytePointer buf = new BytePointer();
        opencv_imgcodecs.imencode(".jpg", sepia, buf);

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
        
        opencv_imgcodecs.imwrite(job.getImageFilename(), sepia);
        return baos;

        // Opcional: salvar em disco

        


    }
    
}
