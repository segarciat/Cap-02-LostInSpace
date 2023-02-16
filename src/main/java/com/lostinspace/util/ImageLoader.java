package com.lostinspace.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class for loading images.
 */
public class ImageLoader {

    /**
     * Loads an image from the resources given by the filename.
     *
     * @param filename Name of the file in the project resources.
     * @return An Image object created from the filename.
     */
    public static Image loadImage(String filename) {
        /*
        try(InputStream inputStream = ImageLoader.class.getResourceAsStream(filename)) {
            return new ImageIcon(ImageIO.read(inputStream)).getImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
        return new ImageIcon(ImageLoader.class.getResource(filename)).getImage();
    }
}
