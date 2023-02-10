package com.lostinspace.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Objects;

class AppGUI {
    private MainFrame frame;
    private ImageIcon bgImage;
    private JLabel bgImageLabel;
    private JPanel panel;
    private MainTextArea textArea;

    private ViewController controller;
    private FrameCreator frameCreator;

    private static String GUILocation;
    private static Iterator<String> iterator;

    // font size
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);

    public static void main(String[] args) {
        AppGUI app = new AppGUI();
        app.execute();
    }

    public AppGUI() {
        bgImage = new ImageIcon();
        bgImageLabel = new JLabel();
        panel = new JPanel();
        textArea = new MainTextArea();

        frame = new MainFrame(panel, textArea);
        frame.setTitle("Lost In Space");
        frame.setSize(736, 758);                           // set frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        controller = new ViewController();
        controller.loadGameObjects();
        this.frameCreator = new FrameCreator();
    }

    public void execute() {
        createTitle();
    }

    private void createTitle() {
        GUILocation = "Title";

        // create timer object
        Timer timer = new Timer(2050, e -> createPrologue());
        timer.setRepeats(false);                                                    // can only execute once

        // set background image icon
        bgImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images_title/title.gif")));

        // add image to label
        bgImageLabel = frameCreator.createBGImageLabel(bgImage);

        /*
         * create button to enter/exit game
         */
        JButton startGameButton = frameCreator.createButtonWithImage("/images_title/start.png", 150, 190, 164, 25);
        JButton exitGameButton = frameCreator.createButtonWithImage("/images_title/exit.png", 409, 190, 164, 25);
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images_title" +
                        "/title_fly" +
                        ".gif")));
                bgImageLabel.setIcon(imageIcon);

                // start timer - after time starts, the program will go into the prologue
                timer.start();
                bgImageLabel.remove(startGameButton);
                bgImageLabel.remove(exitGameButton);

                // set frame as focus
                frame.requestFocus();
            }
        });
        exitGameButton.addActionListener(e -> System.exit(0));

        // add buttons to label
        bgImageLabel.add(startGameButton);
        bgImageLabel.add(exitGameButton);

        frame.setContentPane(bgImageLabel);
        frame.setVisible(true);
    }

    private void createPrologue() {
        GUILocation = "Prologue";

        // set background image icon
        bgImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images_title/background.jpg")));
        bgImageLabel.setIcon(bgImage);

        // initialize iterator
        iterator = controller.getPrologue().lines().iterator();                         // Initialize iterator

        // re-initialize after displaying first 13 lines to avoid pressing the 'enter' key
        iterator = frameCreator.createInitialFullScreenText(textArea, iterator);

        // create skip button
        JButton skipTextButton = frameCreator.createButtonWithImage("/images_title/skip.png", 275, 30, 164, 25);
        skipTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iterator.hasNext()) {
                    // re-initialize, like 'enter' key
                    iterator = frameCreator.createInitialFullScreenText(textArea, iterator);
                }

                // set frame as focus
                frame.requestFocus();
            }
        });

        // set text area attributes
        textArea.setSize(720, 720);                             // size
        textArea.setEditable(false);                                        // non-editable
        textArea.setOpaque(false);                                          // no background
        textArea.setLineWrap(true);                                         // wrap lines
        textArea.setWrapStyleWord(true);                                    // wrap by word
        textArea.setFont(MONOSPACE_PLAIN_MED);                              // font type
        textArea.setForeground(COLOR_GREEN);                                // font color
        textArea.setMargin(new Insets(80,160,0,160)); // margins

        panel.setLayout(new BorderLayout());
        panel.add(skipTextButton, BorderLayout.CENTER);
        panel.add(textArea, BorderLayout.CENTER);
        panel.setSize(720, 720);
        panel.setOpaque(false);

        frame.add(panel);
    }

    static void createInstructions() {
        System.out.println("Hello");
    }

    public static String getGUILocation() {
        return GUILocation;
    }

    public static Iterator<String> getIterator() {
        return iterator;
    }
}
