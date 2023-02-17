package com.lostinspace.util;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class ImageLoaderTest {

    @Test(expected = RuntimeException.class)
    public void loadImage_shouldThrowRunTimeException_whenImageDoesNotExist() {
        ImageLoader.loadImage("fake.png");
    }

    @Test
    public void loadImage_shouldSuccessfullyLoadImage_whenImageExists() {
        String filename = "/images_items/airlock.png";
        try {
            Image image = ImageLoader.loadImage(filename);
            assertNotNull(image);
        } catch(RuntimeException e) {
            fail(String.format("The image %s was not loaded, but it should because it exists.\n", filename));
        }
    }
}