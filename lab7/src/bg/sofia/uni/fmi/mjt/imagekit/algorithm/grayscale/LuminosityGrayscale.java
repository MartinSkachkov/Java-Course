package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    private static final double RED_WEIGHT = 0.21;
    private static final double GREEN_WEIGHT = 0.72;
    private static final double BLUE_WEIGHT = 0.07;

    @Override
    public BufferedImage process(BufferedImage image) {
        validateInput(image);

        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                int grayValue = convertPixelColorToGrayscale(pixelColor);

                Color grayColor = new Color(grayValue, grayValue, grayValue);
                grayscaleImage.setRGB(x, y, grayColor.getRGB());
            }
        }

        return grayscaleImage;
    }

    private int convertPixelColorToGrayscale(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return (int) (RED_WEIGHT * red + GREEN_WEIGHT * green + BLUE_WEIGHT * blue);
    }

    private void validateInput(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Input image cannot be null");
        }
    }
}
