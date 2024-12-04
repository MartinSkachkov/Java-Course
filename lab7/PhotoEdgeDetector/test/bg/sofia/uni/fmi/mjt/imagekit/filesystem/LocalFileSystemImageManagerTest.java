package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LocalFileSystemImageManagerTest {
    private LocalFileSystemImageManager imageManager;

    @BeforeEach
    void setUp() {
        imageManager = new LocalFileSystemImageManager();
    }

    @Test
    void testLoadImageWithNullFile() {
        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImage(null),
                "Expected IllegalArgumentException when loading a null image, but it was not thrown");
    }

    @Test
    void testLoadImageWithNonExistentFile(@TempDir Path tempDir) {
        File nonExistentFile = tempDir.resolve("nonexistent.jpg").toFile();
        assertThrows(IOException.class, () -> imageManager.loadImage(nonExistentFile),
                "Expected IOException when loading a non-existent image, but it was not thrown");
    }

    @Test
    void testLoadImageWithValidFormat(@TempDir Path tempDir) throws IOException {
        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        File imageFile = tempDir.resolve("test.jpg").toFile();
        ImageIO.write(testImage, "jpg", imageFile);

        BufferedImage loadedImage = imageManager.loadImage(imageFile);

        assertNotNull(loadedImage, "The image which was created was null");
        assertEquals(10, loadedImage.getWidth(), "The image that was created has incompatible width");
        assertEquals(10, loadedImage.getHeight(), "The image that was created has incompatible height");
    }

    @Test
    void testLoadImagesFromDirectoryWithAllImageFiles(@TempDir Path tempDir) throws IOException {
        BufferedImage testImage1 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage testImage2 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);

        File imageDir = tempDir.toFile();
        File image1 = new File(imageDir, "test1.jpg");
        File image2 = new File(imageDir, "test2.png");

        ImageIO.write(testImage1, "jpg", image1);
        ImageIO.write(testImage2, "png", image2);

        List<BufferedImage> loadedImages = imageManager.loadImagesFromDirectory(imageDir);

        assertEquals(2, loadedImages.size(),
                "The number of loaded images is incorrect. Expected: 2, but was: " + loadedImages.size());
    }

    @Test
    void testLoadImagesFromDirectoryWithNonImageFIle(@TempDir Path tempDir) throws IOException {
        BufferedImage testImage1 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage testImage2 = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);

        File imageDir = tempDir.toFile();
        File image1 = new File(imageDir, "test1.jpg");
        File image2 = new File(imageDir, "test2.png");
        File nonImageFile = new File(imageDir, "test.txt");

        ImageIO.write(testImage1, "jpg", image1);
        ImageIO.write(testImage2, "png", image2);
        nonImageFile.createNewFile();

        assertThrows(IOException.class, () -> imageManager.loadImagesFromDirectory(imageDir),
                "Expected IOException when loading a non-image file format, but it was not thrown");
    }

    @Test
    void testLoadImagesFromDirectoryWithNullImagesDir() {
        assertThrows(IllegalArgumentException.class, () -> imageManager.loadImagesFromDirectory((null)),
                "Expected IllegalArgumentException when trying to load images from null directory, but it was not thrown");
    }

    @Test
    void testSaveImageWithNullArguments() {
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File outputFile = new File("test.png");

        assertThrows(IllegalArgumentException.class, () -> imageManager.saveImage(null, outputFile),
                "Expected IllegalArgumentException when the image argument is null.");

        assertThrows(IllegalArgumentException.class, () -> imageManager.saveImage(testImage, null),
                "Expected IllegalArgumentException when the output file argument is null.");
    }

    @Test
    void testSaveImage_ValidImage(@TempDir Path tempDir) throws IOException {
        BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File outputFile = tempDir.resolve("output.png").toFile();

        imageManager.saveImage(testImage, outputFile);

        assertTrue(outputFile.exists(), "The image file was not created as expected: " + outputFile.getAbsolutePath());
        BufferedImage savedImage = ImageIO.read(outputFile);
        assertNotNull(savedImage, "The saved image could not be read or is null. Check the output file: " + outputFile.getAbsolutePath());
    }
}
