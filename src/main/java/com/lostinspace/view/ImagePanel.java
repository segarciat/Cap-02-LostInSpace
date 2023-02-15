package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * JPanel with a background image.
 *
 * Original author: Raymo111
 *
 * Source: https://stackoverflow.com/questions/49826647/java-problems-with-gif-in-label
 */
public class ImagePanel extends JPanel {
    private Image backgroundImage;

    public ImagePanel(String filename, int width, int height) {
        backgroundImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(filename)))
                .getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int imageWidth = backgroundImage.getWidth(null);
        int imageHeight = backgroundImage.getHeight(null);

        if (imageWidth == 0 || imageHeight == 0) {
            return;
        }

        double widthScale = (double)getWidth() / (double)imageWidth;
        double heightScale = (double)getHeight() / (double)imageHeight;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawImage(backgroundImage, AffineTransform.getScaleInstance(widthScale, heightScale), this);
        g2d.dispose();
    }

    /**
     * Dimension of the JPanel, which are the background image's height and width.
     *
     * @return A Dimension objects whose width and height are that of the background image.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this));
    }

    /**
     * Returns the current background image painted on the panel.
     * @return The current background image for this panel.
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Sets the background image and repaints the JPanel.
     *
     * @param backgroundImage The new image for the background.
     */
    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
        repaint();
    }
}
