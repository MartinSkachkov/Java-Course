package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.GrayscaleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SobelEdgeDetectionTest {
    private GrayscaleAlgorithm mockGrayscaleAlgorithm;
    private SobelEdgeDetection edgeDetection;

    @BeforeEach
    void setUp() {
        mockGrayscaleAlgorithm = Mockito.mock(GrayscaleAlgorithm.class);
        edgeDetection = new SobelEdgeDetection(mockGrayscaleAlgorithm);
    }

    @Test
    void testProcessingANullImage() {
        assertThrows(IllegalArgumentException.class, () -> edgeDetection.process(null), "Expected IllegalArgumentException when processing a null image, but it was not thrown");
    }

    @Test
    void testProcessEdgeDetection() {
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        when(mockGrayscaleAlgorithm.process(testImage)).thenReturn(createGrayscaleTestImage());

        BufferedImage edgeDetectedImage = edgeDetection.process(testImage);

        assertNotNull(edgeDetectedImage);
        assertEquals(100, edgeDetectedImage.getWidth());
        assertEquals(100, edgeDetectedImage.getHeight());

        verify(mockGrayscaleAlgorithm).process(testImage);
    }

    private BufferedImage createGrayscaleTestImage() {
        BufferedImage grayscaleImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < 100; y++) {
            for (int x = 0; x < 100; x++) {
                Color color = new Color(grayscaleImage.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();

                int grayValue = (int) (0.21 * red + 0.72 * green + 0.07 * blue);
                Color grayColor = new Color(grayValue, grayValue, grayValue);
                grayscaleImage.setRGB(x, y, grayColor.getRGB());
            }
        }

        return grayscaleImage;
    }
}
