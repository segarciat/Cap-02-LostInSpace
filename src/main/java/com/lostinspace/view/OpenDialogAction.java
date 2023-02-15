package com.lostinspace.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenDialogAction implements ActionListener {

    // window settings, font text, font color
    private static final int WINDOW_SIZE = 720;
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);
    private static final java.awt.Color COLOR_GREEN = new Color(76, 175, 82);

    // fields for the frame, text area, title, and buttons
    private JFrame frame;
    private JTextArea textArea = new JTextArea();
    private String title;
    private JButton closeButton;
    private JDialog d;

    public OpenDialogAction(JFrame frame, String text, String title) {
        this.frame = frame;
        setTextAreaOptions(textArea, text);
        this.title = title;
        this.closeButton = new JButton("close");
        this.closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
    }

    /// actions for image settings in the dialog frame
    @Override
    public void actionPerformed(ActionEvent e) {
        ImagePanel imgPanel = new ImagePanel("/images_title/background.jpg", 720, 720);
        d = new JDialog(frame, title);
        imgPanel.add(textArea);
        imgPanel.add(closeButton);
        d.setSize(WINDOW_SIZE, WINDOW_SIZE);
        d.setContentPane(imgPanel);
        d.setVisible(true);

    }

    // Set text area attributes
    private static void setTextAreaOptions(JTextArea textArea, String text) {
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
