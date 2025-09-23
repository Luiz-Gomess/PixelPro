package com.example.pixelpro.consumers.strategy;

import java.io.ByteArrayOutputStream;

public interface ImageProcessorStrategy {
    ByteArrayOutputStream process (byte[] data);
}
