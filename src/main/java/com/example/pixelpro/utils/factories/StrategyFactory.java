package com.example.pixelpro.utils.factories;

import com.example.pixelpro.consumers.strategy.ImageProcessorStrategy;
import com.example.pixelpro.consumers.strategy.impl.BlurStrategy;
import com.example.pixelpro.consumers.strategy.impl.GrayscaleStrategy;
import com.example.pixelpro.consumers.strategy.impl.InvertColorsStrategy;
import com.example.pixelpro.consumers.strategy.impl.SepiaStrategy;
import com.example.pixelpro.enums.OperationType;

public abstract class StrategyFactory {
    
    public static ImageProcessorStrategy getStrategy (OperationType operation) {
        return switch (operation) {
            case OperationType.GRAYSCALE -> new GrayscaleStrategy();
            case OperationType.SEPIA -> new SepiaStrategy();
            case OperationType.INVERT -> new InvertColorsStrategy();
            case OperationType.BLUR -> new BlurStrategy();
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }
}
