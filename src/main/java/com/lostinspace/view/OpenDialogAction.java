package com.lostinspace.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenDialogAction implements ActionListener {
    private static final String BUTTON_CLOSE = "/images_title/close.png";
    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 40;

    // window settings, font text, font color
    private static final int WINDOW_SIZE = 720;
    public static final String DIALOG_BACKGROUND = "/images_title/background.jpg";

    // fields for the frame, text area, title, and buttons
    private final JFrame frame;
    private final JTextArea textArea;
    private final String title;
    private final JButton closeButton;
    private JDialog d;

    public OpenDialogAction(JFrame frame, String text, String title) {
        this.frame = frame;
        this.title = title;

        textArea = SwingComponentCreator.createStyledTextArea(text);

        this.closeButton = SwingComponentCreator.createButtonWithImage(BUTTON_CLOSE, 235, 620, BUTTON_WIDTH, BUTTON_HEIGHT);
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
        ImagePanel imgPanel = new ImagePanel(DIALOG_BACKGROUND);
        imgPanel.setLayout(null);

        d = new JDialog(frame, title);
        d.setSize(WINDOW_SIZE, WINDOW_SIZE);
        d.setContentPane(imgPanel);
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        imgPanel.add(textArea);
        imgPanel.add(closeButton);

        // Set Z-order to ensure the close button is at the top, so the button can be pressed
        imgPanel.setComponentZOrder(textArea, 1);
        imgPanel.setComponentZOrder(closeButton, 0);
    }
}
