package com.lostinspace.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpInstructions {

    public static void main(String[] args) {
        String msg = "<html>objectives: check game winning objectives\n" +
                "\n" +
                "look room: inspects the current room you're in for items, interactable objects, and exits\n" +
                "\n" +
                "inspect item -or- inspect object: receive a description of what was inspected, look inside of containers\n" +
                "<the inspect command will often reveal details about something you are confused about>\n" +
                "\n" +
                "go direction: move in selected direction\n" +
                "<directions: North, South, East, West>\n" +
                "\n" +
                "use item: use an item in your inventory or an object in the same room as you\n" +
                "<not all items can be used at all times or in every room. Experiment with your options!>\n" +
                "\n" +
                "restart: restart the game\n" +
                "\n" +
                "exit: quits the current game.\n" +
                "\n" +
                "easymode: too hard? Use this command to never use oxygen when moving.";
        JOptionPane optionPane = new NarrowOptionPane();
        optionPane.setMessage(msg);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(null, "Help Instructions");
        dialog.setVisible(true);
    }
}
