package com.lostinspace.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends ImagePanel {

    public static final String BACKGROUND = "/images_title/background.jpg";
    public static final String HELP_DIALOG_TITLE = "instructions";

    public MenuPanel(AppGUI app) {
        super(BACKGROUND, app.getFrame().getWidth(), app.getFrame().getHeight());

        this.setSize(app.getFrame().getWidth(), app.getFrame().getHeight());
        this.setLayout(new GridBagLayout());

        // buttons created for menu
        JButton exitButton = SwingComponentCreator.createButtonWithText("Exit");

        JButton helpButton = SwingComponentCreator.createButtonWithText("Help");

        JButton continueButton = SwingComponentCreator.createButtonWithText("Continue");

        // action listener for exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel panel = this;

        // action listener for help button
        helpButton.addActionListener(new OpenDialogAction(app.getFrame(), app.getController().getInstructions(), HELP_DIALOG_TITLE));

        // action listener for help button
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.setRoute(Route.GAME);
                app.update();
            }
        });

        // add exit, help, and continue button to panel in a grid layout with transparent buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 12, 0);
        panel.add(helpButton, gbc);
        gbc.gridy = 1;
        panel.add(exitButton, gbc);
        gbc.gridy = 2;
        panel.add(continueButton, gbc);
    }
}










