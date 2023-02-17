package com.lostinspace.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for loading images.
 */
public class ImageLoader {

    public static final String GIF_REGEX = "(?i)(.*\\.gif)";

    /**
     * Loads an image from the resources given by the filename.
     *
     * @param filename Name of the file in the project resources.
     * @return An Image object created from the filename.
     */
    public static Image loadImage(String filename) {
        try(InputStream inputStream = ImageLoader.class.getClassLoader().getResourceAsStream(filename)) {
            if (filename.matches(GIF_REGEX)) {
                byte[] imageBytes = inputStream.readAllBytes();
                return Toolkit.getDefaultToolkit().createImage(imageBytes);
            } else {
                return ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
