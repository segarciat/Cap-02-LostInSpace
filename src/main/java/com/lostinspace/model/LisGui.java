package com.lostinspace.model;

import javax.swing.*;
import java.awt.*;

public class LisGui {

    public static void main(String[] args) {

//        ImageIcon image = new ImageIcon("/rocket.png");

        //JLabel for string or image
        JLabel label = new JLabel("Lost In Space"); // creates a label
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.TOP);
//        label.setIcon(image);
        label.setForeground(Color.BLUE); // set color of text
        label.setFont(new Font("SansSerif", Font.ITALIC, 35)); //set font of text

        // JFrame window for GUI for components
        JFrame frame = new JFrame(); // creates an instance of frame
        frame.setTitle(" Lost in Space "); // Title of frame
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of application
//        frame.setResizable(false); // frame cannot be resized
        frame.setSize(420, 420); // X and Y dimension of the frame
        frame.getContentPane().setBackground(Color.black); // changes color of background
        frame.setVisible(true); // shows a visible frame

    }

}
