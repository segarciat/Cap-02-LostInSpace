package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenDialogAction implements ActionListener {
    private static final int WINDOW_SIZE = 720;
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);
    // font colors
    private static final java.awt.Color COLOR_GREEN = new Color(76, 175, 82);

    private JFrame frame;
    JTextArea textArea =  new JTextArea();
    String title;

    public OpenDialogAction(JFrame frame, String text, String title) {
        this.frame = frame;
        setTextAreaOptions(textArea, text);
        this.title = title;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel imgPanel = new ImagePanel("/images_title/background.jpg", 720, 720);
        JDialog d = new JDialog(frame, title);
        imgPanel.add(textArea);
        d.setSize(WINDOW_SIZE, WINDOW_SIZE);
        d.setContentPane(imgPanel);
        d.setVisible(true);
    }

    private static void setTextAreaOptions(JTextArea textArea, String text) {
        // Set text area attributes
        textArea.setText(text);                        // initial text
        textArea.setSize(WINDOW_SIZE, WINDOW_SIZE);                          // size
        textArea.setEditable(false);                                         // non-editable
        textArea.setFocusable(false);                                       // prevent from stealing focus on click
        textArea.setOpaque(false);                                           // no background
        textArea.setLineWrap(true);                                          // wrap lines
        textArea.setWrapStyleWord(true);                                     // wrap by word
        textArea.setFont(MONOSPACE_PLAIN_MED);                               // font type
        textArea.setForeground(COLOR_GREEN);                                 // font color
        textArea.setMargin(new Insets(0, 140, 0, 140));  // margins
    }
}
