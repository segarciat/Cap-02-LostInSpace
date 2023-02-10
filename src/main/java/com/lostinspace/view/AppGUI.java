package com.lostinspace.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Objects;

class AppGUI {
    private JFrame frame;
    private ImageIcon bgImage;
    private JLabel bgImageLabel;
    private JPanel panel;
    private JTextArea textArea;
    private JButton skipButton;
    private ActionListener skipButtonAction;

    private ViewController controller;
    private FrameCreator frameCreator;

    private Route route = new Route("Title");
    private Iterator<String> iterator;

    // font size
    private static final Font MONOSPACE_PLAIN_MED = new Font("Monospaced", Font.PLAIN, 14);

    // font colors
    private static final Color COLOR_GREEN = new Color(76, 175, 82);

    public static void main(String[] args) {
        AppGUI app = new AppGUI();
        app.execute();
    }

    /*
     * GUI constructor
     * initializes the fields, controllers, and JFrame
     */
    public AppGUI() {
        bgImage = new ImageIcon();
        bgImageLabel = new JLabel();
        panel = new JPanel();
        textArea = new JTextArea();
        skipButton = new JButton();
        skipButtonAction = e -> {};

        frame = new JFrame();
        frame.setTitle("Lost In Space");

        frame.setSize(736, 758);                           // set frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        frame.addKeyListener(new KeyListener() {
            private boolean isMovingOn = false;

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    route = getRoute();
                    Iterator<String> iterator = getIterator();

                    if (!iterator.hasNext()) {
                        isMovingOn = true;
                    }

                    switch(route.getRoute()) {
                        case "Title":
                            break;
                        case "Prologue":
                            if (isMovingOn) {
                                setRoute(new Route("Tutorial"));
                                execute();
                            } else {
                                setTextArea(iterator);
                            }
                            break;
                        case "Tutorial":
                            if (isMovingOn) {
                                setRoute(new Route("Game"));
                                execute();
                            } else {
                                setTextArea(iterator);
                            }
                        default:
                            break;
                    }

                    isMovingOn = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        controller = new ViewController();
        controller.loadGameObjects();
        this.frameCreator = new FrameCreator();
    }

    public void execute() {
        switch(route.getRoute()) {
            case "Title":
                createTitle();
                break;
            case "Prologue":
                createPrologue();
                break;
            case "Tutorial":
                createTutorial();
                break;
            case "Game":
                createGame();
                break;
            default:
                break;
        }
    }

    private void createTitle() {
        // create timer object
        Timer timer = new Timer(2050, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                route.setRoute("Prologue");
                execute();
            }
        });
        timer.setRepeats(false);                                                    // can only execute once

        // set background image icon
        bgImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images_title/title.gif")));

        // add image to label
        bgImageLabel = frameCreator.createBGImageLabel(bgImage);

        /*
         * create button to enter/exit game
         */
        JButton startGameButton = frameCreator.createButtonWithImage("/images_title/start.png", 100, 175, 250, 40);
        JButton exitGameButton = frameCreator.createButtonWithImage("/images_title/exit.png", 370, 175, 250, 40);

        // create action listeners
        ActionListener startGameButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images_title" +
                        "/title_fly" +
                        ".gif")));
                bgImageLabel.setIcon(imageIcon);

                // start timer - after time starts, the program will go into the prologue
                timer.start();

                // remove buttons after timer stops
                bgImageLabel.remove(startGameButton);
                bgImageLabel.remove(exitGameButton);

                // set frame as focus
                frame.requestFocus();
            }
        };

        ActionListener exitGameButtonAction = e -> System.exit(0);

        startGameButton.addActionListener(startGameButtonAction);
        exitGameButton.addActionListener(exitGameButtonAction);

        // add buttons to label
        bgImageLabel.add(startGameButton);
        bgImageLabel.add(exitGameButton);

        frame.setContentPane(bgImageLabel);
        frame.setVisible(true);
    }

    private void createPrologue() {
        // set background image icon
        bgImage = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/images_title/background.jpg")));
        bgImageLabel.setIcon(bgImage);

        // initialize iterator
        iterator = controller.getPrologue().lines().iterator();                         // Initialize iterator

        // re-initialize after displaying first 13 lines to avoid pressing the 'enter' key
        iterator = frameCreator.createInitialFullScreenText(textArea, iterator);

        // create skip button
        skipButton = frameCreator.createButtonWithImage("/images_title/skip.png", 235, 40, 250, 40);

        // create action listener
        skipButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iterator.hasNext()) {
                    // re-initialize, like 'enter' key
                    iterator = frameCreator.createInitialFullScreenText(textArea, iterator);
                } else {
                    skipButton.removeActionListener(skipButtonAction);
                    setRoute(new Route("Tutorial"));
                    execute();
                }

                // set frame as focus
                frame.requestFocus();
            }
        };

        skipButton.addActionListener(skipButtonAction);

        // set text area attributes
        textArea.setSize(720, 720);                             // size
        textArea.setEditable(false);                                        // non-editable
        textArea.setOpaque(false);                                          // no background
        textArea.setLineWrap(true);                                         // wrap lines
        textArea.setWrapStyleWord(true);                                    // wrap by word
        textArea.setFont(MONOSPACE_PLAIN_MED);                              // font type
        textArea.setForeground(COLOR_GREEN);                                // font color
        textArea.setMargin(new Insets(80,160,0,160)); // margins

        // set panel attributes
        panel.setLayout(new BorderLayout());
        panel.add(skipButton, BorderLayout.PAGE_END);
        panel.add(textArea, BorderLayout.CENTER);
        panel.setSize(720, 720);
        panel.setOpaque(false);

        // add panel to frame
        frame.add(panel);
    }

    public void createTutorial() {
        // re-initialize skip button action and re-add
        skipButtonAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRoute(new Route("Game"));
                execute();

                // set frame as focus
                frame.requestFocus();
            }
        };
        skipButton.addActionListener(skipButtonAction);

        iterator = controller.getTutorialsText().lines().iterator();                         // Initialize iterator
        // re-initialize after displaying first 13 lines to avoid pressing the 'enter' key
        iterator = frameCreator.createInitialFullScreenText(textArea, iterator);
    }

    private void createGame() {
        // remove skip button action and button from panel
        skipButton.removeActionListener(skipButtonAction);
        panel.remove(skipButton);

        textArea.setText("HI");
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Iterator<String> getIterator() {
        return iterator;
    }

    private void setTextArea(Iterator <String> iterator) {
        frameCreator.createSubsequentFullScreenText(textArea, iterator);
    }
}
