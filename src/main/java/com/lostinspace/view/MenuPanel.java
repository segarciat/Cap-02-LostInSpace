package com.lostinspace.view;

import com.lostinspace.app.App;
import com.lostinspace.app.GUIApp;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuPanel extends ImagePanel {

    public static final String BACKGROUND = "/images_title/background.jpg";
    public static final String HELP_DIALOG_TITLE = "instructions";
    public static final String OBJECTIVES_DIALOG_TITLE = "objectives";

    public MenuPanel(AppView app) {
        super(BACKGROUND, app.getFrame().getWidth(), app.getFrame().getHeight());

        this.setSize(app.getFrame().getWidth(), app.getFrame().getHeight());
        this.setLayout(new GridBagLayout());

        // buttons created for menu
        JButton exitButton = SwingComponentCreator.createButtonWithText("Exit");

        JButton helpButton = SwingComponentCreator.createButtonWithText("Help");

        JButton continueButton = SwingComponentCreator.createButtonWithText("Continue");

        JButton objectiveButton = SwingComponentCreator.createButtonWithText("Objectives");

        JButton easyModeButton = SwingComponentCreator.createButtonWithText("Easy Mode");

        JButton restartButton = SwingComponentCreator.createButtonWithText("Restart");

        // action listener for exit button
        exitButton.addActionListener(e -> System.exit(0));

        JPanel panel = this;

        // action Listener for objectives button
        objectiveButton.addActionListener(new OpenDialogAction(app.getFrame(), app.getController().getObjectives(), OBJECTIVES_DIALOG_TITLE));

        // action listener for help button
        helpButton.addActionListener(new OpenDialogAction(app.getFrame(), app.getController().getInstructions(), HELP_DIALOG_TITLE));

        // action listener for restart button
        restartButton.addActionListener(e -> GUIApp.main(null));

        // action listener for easy mode  button
        easyModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isEasyMode = app.getController().toggleEasyMode();
                // declare a string variable for dialog message
                String dialogText;
                // using isEasyMode, assign correct message
                if (isEasyMode){
                    dialogText = "Easy Mode is activated";
                } else {
                    dialogText = "Easy Mode is deactivated";
                }

                // pass dialog message to show message dialog method
                JOptionPane.showMessageDialog(getComponentPopupMenu(),dialogText);

            }
        });

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
        panel.add(continueButton, gbc);
        gbc.gridy = 1;
        panel.add(objectiveButton, gbc);
        gbc.gridy = 2;
        panel.add(easyModeButton, gbc);
        gbc.gridy = 3;
        panel.add(helpButton, gbc);
        gbc.gridy = 4;
        panel.add(restartButton, gbc);
        gbc.gridy = 5;
        panel.add(exitButton, gbc);
    }
}










