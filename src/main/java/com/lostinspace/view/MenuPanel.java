package com.lostinspace.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends ImagePanel {
    public static final String BACKGROUND = "/images_title/background.jpg";
    public static final String HELP_DIALOG_TITLE = "instructions";
    public static final String OBJECTIVES_DIALOG_TITLE = "objectives";

    public MenuPanel(AppView view) {
        super(BACKGROUND);

        this.setSize(view.getFrame().getWidth(), view.getFrame().getHeight());
        this.setLayout(new GridBagLayout());

        // buttons created for menu
        JButton exitButton = SwingComponentCreator.createExitButton();

        JButton helpButton = SwingComponentCreator.createButtonWithText("Help");

        JButton continueButton = SwingComponentCreator.createButtonWithText("Continue");

        JButton objectiveButton = SwingComponentCreator.createButtonWithText("Objectives");

        JButton easyModeButton = SwingComponentCreator.createButtonWithText("Easy Mode");

        JButton restartButton = SwingComponentCreator.createRestartButton(view);


        JPanel panel = this;

        // action Listener for objectives button
        objectiveButton.addActionListener(new OpenDialogAction(view.getFrame(), view.getController().getObjectives(), OBJECTIVES_DIALOG_TITLE));

        // action listener for help button
        helpButton.addActionListener(new OpenDialogAction(view.getFrame(), view.getController().getInstructions(), HELP_DIALOG_TITLE));

        // action listener for easy mode  button
        easyModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isEasyMode = view.getController().toggleEasyMode();
                // declare a string variable for dialog message
                String dialogText;
                // using isEasyMode, assign correct message
                if (isEasyMode) {
                    dialogText = "Easy Mode is activated";
                } else {
                    dialogText = "Easy Mode is deactivated";
                }

                // pass dialog message to show message dialog method
                JOptionPane.showMessageDialog(view.getFrame(), dialogText);
            }
        });

        // action listener for help button
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.setRoute(Route.GAME);
                view.update();
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










