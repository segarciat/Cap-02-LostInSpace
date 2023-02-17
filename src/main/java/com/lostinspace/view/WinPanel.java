package com.lostinspace.view;

import com.lostinspace.util.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class WinPanel extends ImagePanel {
    private static final String INITIAL_IMAGE = "/images_title/end_flyout.gif";
    private static final String END_IMAGE = "/images_title/title.gif";
    private static final int TIMER_DELAY = 6000;

    public WinPanel(AppView app) {
        super(INITIAL_IMAGE, app.getFrame().getWidth(), app.getFrame().getHeight());
        this.setSize(app.getFrame().getWidth(), app.getFrame().getHeight());
        this.setLayout(null);

        Timer timer = new Timer(TIMER_DELAY, e1 -> {
            this.setBackgroundImage(ImageLoader.loadImage(END_IMAGE));
        });
        timer.setRepeats(false);

        timer.start();
    }
}
