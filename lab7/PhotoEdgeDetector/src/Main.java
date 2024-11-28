import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FileSystemImageManager fsImageManager = new LocalFileSystemImageManager();

        BufferedImage image;
        try {
            image = fsImageManager.loadImage(new File("C:\\Users\\Marto\\Desktop\\Java\\lab7\\resources\\car.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageAlgorithm grayscaleAlgorithm = new LuminosityGrayscale();
        BufferedImage grayscaleImage = grayscaleAlgorithm.process(image);

        ImageAlgorithm sobelEdgeDetection = new SobelEdgeDetection(grayscaleAlgorithm);
        BufferedImage edgeDetectedImage = sobelEdgeDetection.process(image);
        try {
            fsImageManager.saveImage(grayscaleImage, new File("car-grayscale.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            fsImageManager.saveImage(edgeDetectedImage, new File("car-edge-detected.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}