package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MapPanel extends ImagePanel {
    private static final String BACKGROUND = "/images_title/map.png";
    private static final String BUTTON_CLOSE = "/images_title/close.png";
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;

    public MapPanel(AppView view) {
        super(BACKGROUND);
        this.setSize(view.getFrame().getWidth(), view.getFrame().getHeight());
        this.setLayout(null);

        JPanel panel = this;

        JButton closeButton = SwingComponentCreator.createButtonWithImage(BUTTON_CLOSE, 235, 620, BUTTON_WIDTH,
                BUTTON_HEIGHT);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setRoute(Route.GAME);
                view.update();
            }
        });

        panel.add(closeButton);
    }
}
