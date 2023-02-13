package com.lostinspace.util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ImageLoader {
    public static Image loadImage(String filename) {
        return new ImageIcon(Objects.requireNonNull(ImageLoader.class.getResource(filename))).getImage();
    }
}
