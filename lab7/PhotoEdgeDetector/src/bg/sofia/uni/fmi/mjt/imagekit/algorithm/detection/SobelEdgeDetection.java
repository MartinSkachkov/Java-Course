package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    private static final int[][] KERNEL_G_X = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    private static final int[][] KERNEL_G_Y = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    private static final int PIXEL_MAX_VALUE = 255;
    private final ImageAlgorithm grayscaleAlgorithm;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        if (!(grayscaleAlgorithm instanceof GrayscaleAlgorithm)) {
            throw new IllegalArgumentException("Grayscale algorithm must be an instance of GrayscaleAlgorithm");
        }

        this.grayscaleAlgorithm = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        validateInput(image);

        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();

        BufferedImage edgeDetectedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int[] result = applySobelKernels(grayscaleImage, x, y);
                int sumX = result[0];
                int sumY = result[1];

                int magnitude = calculateMagnitudeAndNormalize(sumX, sumY);
                Color edgeColor = new Color(magnitude, magnitude, magnitude);
                edgeDetectedImage.setRGB(x, y, edgeColor.getRGB());
            }
        }

        return edgeDetectedImage;
    }

    private int[] applySobelKernels(BufferedImage grayscaleImage, int x, int y) {
        int sumX = 0;
        int sumY = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Color pixel = new Color(grayscaleImage.getRGB(x + j, y + i));
                int grayValue = pixel.getRed();

                sumX += grayValue * KERNEL_G_X[i + 1][j + 1];
                sumY += grayValue * KERNEL_G_Y[i + 1][j + 1];
            }
        }

        int[] result = new int[2];
        result[0] = sumX;
        result[1] = sumY;

        return result;
    }

    private int calculateMagnitudeAndNormalize(int sumX, int sumY) {
        int magnitude = (int) Math.sqrt(sumX * sumX + sumY * sumY);
        return Math.min(magnitude, PIXEL_MAX_VALUE);
    }

    private void validateInput(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Input image cannot be null");
        }
    }
}
