package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LuminosityGrayscaleTest {
    private LuminosityGrayscale grayscaleAlgorithm;

    @BeforeEach
    void setup() {
        grayscaleAlgorithm = new LuminosityGrayscale();
    }

    @Test
    void testProcessingANullImageToGrayscale() {
        assertThrows(IllegalArgumentException.class, () -> grayscaleAlgorithm.process(null),
                "The process method should throw IllegalArgumentException when input image is null");
    }

    @Test
    void testProcessingAColorImageToGrayscale() {
        BufferedImage colorImage = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);

        colorImage.setRGB(0, 0, new Color(255, 0, 0).getRGB());
        colorImage.setRGB(1, 0, new Color(0, 255, 0).getRGB());
        colorImage.setRGB(2, 0, new Color(0, 0, 255).getRGB());

        BufferedImage grayscaleImage = grayscaleAlgorithm.process(colorImage);

        Color redGrayscale = new Color(grayscaleImage.getRGB(0, 0));
        Color greenGrayscale = new Color(grayscaleImage.getRGB(1, 0));
        Color blueGrayscale = new Color(grayscaleImage.getRGB(2, 0));

        assertEquals(53, redGrayscale.getRed(), "Red pixel grayscale value doesn't match the red grayscale intensity");
        assertEquals(183, greenGrayscale.getGreen(), "Green pixel grayscale value doesn't match the green grayscale intensity");
        assertEquals(17, blueGrayscale.getBlue(), "Blue pixel grayscale value doesn't match the blue grayscale intensity");
    }

    @Test
    void testIfProcessingKeepsTheImageDimensions() {
        BufferedImage colorImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);

        BufferedImage grayscaleImage = grayscaleAlgorithm.process(colorImage);

        assertEquals(100, grayscaleImage.getWidth(), "The grayscale image should have the same width as the original image");
        assertEquals(200, grayscaleImage.getHeight(), "The grayscale image should have the same height as the original image");
    }
}
