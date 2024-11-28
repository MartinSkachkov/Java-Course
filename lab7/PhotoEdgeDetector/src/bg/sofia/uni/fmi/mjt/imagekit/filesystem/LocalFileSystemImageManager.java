package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    private static final String[] SUPPORTED_IMAGE_FORMATS = {"jpeg", "png", "bmp", "jpg"};

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        validateInput(imageFile);
        validateImageFileConstraints(imageFile);

        return ImageIO.read(imageFile);
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        validateInput(imagesDirectory);
        validateDirectoryConstraints(imagesDirectory);

        List<BufferedImage> images = new ArrayList<>();
        File[] filesInDirectory = imagesDirectory.listFiles();

        if (filesInDirectory == null) {
            return images;
        }

        for (File file : filesInDirectory) {
            if (file.isFile() && isSupportedImageFormat(file)) {
                images.add(loadImage(file));
            }
        }

        return images;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        validateSaveImageInput(image, imageFile);
        validateSavingImageConstraints(imageFile);

        if (isSupportedImageFormat(imageFile)) {
            ImageIO.write(image, getFileExtension(imageFile), imageFile);
        } else {
            throw new IOException("Unable to save image: unsupported format");
        }
    }

    private void validateInput(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Passed function parameter cannot be null");
        }
    }

    private void validateSaveImageInput(BufferedImage image, File imageFile) {
        if (image == null || imageFile == null) {
            throw new IllegalArgumentException("Image and image file cannot be null");
        }
    }

    private void validateImageFileConstraints(File imageFile) throws IOException {
        if (!imageFile.exists()) {
            throw new IOException("Image file does not exist");
        }

        if (!imageFile.isFile()) {
            throw new IOException("Provided path is not a file");
        }

        if (!isSupportedImageFormat(imageFile)) {
            throw new IOException("Unsupported image format");
        }
    }

    private void validateDirectoryConstraints(File directory) throws IOException {
        if (!directory.exists()) {
            throw new IOException("Directory does not exist");
        }

        if (!directory.isDirectory()) {
            throw new IOException("Provided path is not a directory");
        }
    }

    private void validateSavingImageConstraints(File imageFile) throws IOException {
        File parentDir = imageFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            throw new IOException("Unable to save image: the parent directory does not exist.");
        }

        if (imageFile.exists()) {
            throw new IOException("Unable to save image: the file already exists.");
        }
    }

    private boolean isSupportedImageFormat(File imageFile) {
        String fileExtension = getFileExtension(imageFile);

        for (String format : SUPPORTED_IMAGE_FORMATS) {
            if (fileExtension.equals(format)) {
                return true;
            }
        }

        return false;
    }

    private String getFileExtension(File imageFile) {
        String fileName = imageFile.getName();
        int dotIndex = fileName.lastIndexOf('.');

        return dotIndex > 0 ? fileName.substring(dotIndex + 1).toLowerCase() : "";
    }

}
