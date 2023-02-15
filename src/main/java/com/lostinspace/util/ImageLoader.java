package com.lostinspace.util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

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
        return new ImageIcon(Objects.requireNonNull(ImageLoader.class.getResource(filename))).getImage();
    }
}
