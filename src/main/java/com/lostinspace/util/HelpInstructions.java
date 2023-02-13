package com.lostinspace.util;

import com.lostinspace.view.ImagePanel;
import com.lostinspace.view.OpenDialogAction;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class HelpInstructions {

    public static void main(String[] args) {

        final JFrame frame = new JFrame("Lost In Space"); // set text title for gui frame
        frame.setSize(720, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes window and terminates
        frame.setLocationRelativeTo(null);

        String msg = TextLoader.loadText("text/instructions.txt");
        JButton button = new JButton("Help");
        frame.add(button);

        button.addActionListener(new OpenDialogAction(frame, msg, "instructions" ));

        frame.setVisible(true);
    }
}
