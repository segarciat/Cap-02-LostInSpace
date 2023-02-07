package com.lostinspace.model;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.*;

public class LisGui {

    public static void main(String[] args) {

        // JFrame window for GUI for components

        JFrame frame = new JFrame(); // creates an instance of frame
        frame.setTitle(" Lost in Space "); // Title of frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of application
//        frame.setResizable(false); // frame cannot be resized
        frame.setSize(620,620); // X and Y dimension of the frame
        frame.setVisible(true); // shows a visible frame

        ImageIcon image = new ImageIcon("images/Lis.png"); // image icon created
        frame.setIconImage(image.getImage()); // changes icon on frame
        frame.getContentPane().setBackground(Color.BLACK); // changes color of background
    }

}
