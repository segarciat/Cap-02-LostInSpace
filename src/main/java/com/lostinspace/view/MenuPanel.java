package com.lostinspace.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends ImagePanel {


    public MenuPanel() {
//        super();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        // exit Button
        JButton exitButton = new JButton("Exit");

        // help Button
        JButton helpButton = new JButton("Help");

        // action listener for exit button
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // action listener for help button
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel, "Eventually will show help text");
            }
        });

        // add exit and help button to panel in a box layout with transparent buttons
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        panel.add(exitButton);
        panel.add(helpButton);
        exitButton.setBackground(new Color(0,0,0,65));
        helpButton.setBackground(new Color(0,0,0, 65));

        // add panel to the JFrame that includes buttons
        frame.add(panel);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}









