package com.lostinspace.controller;

import com.lostinspace.model.Room;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class GameWindow {
    private static final String TITLE_SCREEN_IMAGE = "/images_title/title_fly.gif";
    private static final String BACKGROUND_IMAGE = "/images_title/background.jpg";


    private static final String GAME_TITLE = "Lost In Space";

    private static final int WIDTH = 720;
    private static final int HEIGHT = 720;


    private final Controller controller;

    JFrame frame = new JFrame(GAME_TITLE);

    // Has background splash GIF image and enter game button
    JLabel titlePane;

    // Has starry image background and a text area / label text
    // Responds to enter to move to next portion of text in the sequence
    // Has a skip button that skips the intro sequence
    JLabel introPane;

    JLabel roomPane;

    // Each room is a separate pane.
    Map<String, JLabel> roomPanes;

    public GameWindow() {
        controller = new Controller();
        buildTitlePane();
        buildIntroSequencePane();
        buildRoomLabels();
        buildInventoryArea();
        setFrameOptions();
    }

    private void buildInventoryArea() {
        //
        controller.getInventory();

    }

    private void buildRoomLabels() {
        roomPanes = new HashMap<>();
        for (String roomName: controller.getRoomMap().keySet()) {
            roomPanes.put(roomName, buildRoomPane(roomName));
        }

        System.out.println(roomPanes.size());

    }

    private JLabel buildRoomPane(String roomName) {
        Image bgImage = new ImageIcon(this.getClass().getResource(BACKGROUND_IMAGE))
                .getImage()
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);

        roomPane = new JLabel(new ImageIcon(bgImage));
        roomPane.setSize(WIDTH, HEIGHT);
        roomPane.setLayout(new GridBagLayout());

        Map<String, Room> roomMap = controller.getRoomMap();

        Room currentRoom = roomMap.get(roomName);
        // Make a cardinal button for each exit
        Map<String, String> exits = currentRoom.getExits();

        JTextArea roomTextArea = new JTextArea(currentRoom.getDescription());
        roomTextArea.setForeground(Color.GREEN);
        roomTextArea.setPreferredSize(new Dimension(480, 360));
        roomTextArea.setLineWrap(true);
        roomTextArea.setWrapStyleWord(true);
        roomTextArea.setOpaque(false);
        roomTextArea.setEditable(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        roomPane.add(roomTextArea, gbc);

        JPanel buttonPane = new JPanel();
        buttonPane.setOpaque(false);

        for (String dir: exits.keySet()) {
            JButton dirBtn = new JButton(String.format("Go %s", dir));
            dirBtn.addActionListener((e) -> {
                String exitRoomName = exits.get(dir);
                frame.setContentPane(roomPanes.get(exitRoomName));
            });

            buttonPane.add(dirBtn);
        }

        gbc.gridy = 1;
        roomPane.add(buttonPane, gbc);
        return roomPane;
    }

    private void buildIntroSequencePane() {
        Image bgImage = new ImageIcon(this.getClass().getResource(BACKGROUND_IMAGE))
                .getImage()
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);

        introPane = new JLabel(new ImageIcon(bgImage));
        introPane.setSize(WIDTH, HEIGHT);
        introPane.setLayout(new GridBagLayout());

        JLabel pressEnter = new JLabel("Press Enter");
        pressEnter.setFont(new Font("Arial", Font.BOLD, 20));
        pressEnter.setForeground(Color.GREEN);

        List<String> introSequenceList = new ArrayList<>(controller.buildPrologueParts());
        introSequenceList.add(controller.getInstructions());

        Iterator<String> iterator = introSequenceList.iterator();

        JTextArea introText = new JTextArea(iterator.next());
        JScrollPane scrollPane = new JScrollPane(introText);
        scrollPane.setPreferredSize(new Dimension(480, 360));
        introText.setLineWrap(true);
        introText.setWrapStyleWord(true);
        introText.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBackground(Color.GREEN);
        scrollPane.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0, 255, 0, 60);
            }
        });
        scrollPane.getViewport().setOpaque(false);
        introText.setEditable(false);

        introPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (iterator.hasNext()) {
                        introText.setText(iterator.next());
                        introText.setCaretPosition(0);
                    }
                    else {
                        String startingLocation = controller.getPlayer().getCurrentRoom();
                        frame.setContentPane(roomPanes.get(startingLocation));
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JButton skipBtn = new JButton("Skip");
        // skipBtn.setBounds(250, 250, 100, 100);

        skipBtn.addActionListener((e) -> {
            String startingLocation = controller.getPlayer().getCurrentRoom();
            frame.setContentPane(roomPanes.get(startingLocation));
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        introPane.add(pressEnter, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        introPane.add(scrollPane, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        introPane.add(skipBtn, gbc);

        frame.setContentPane(titlePane);
    }

    private void setFrameOptions() {
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack(); // makes frame take only as much space as its components
        frame.setResizable(false); // do not allow player to resize the room
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // center the window in the player's screen
        frame.setVisible(true); // show the window.
    }



    private void buildTitlePane() {
        // Make label
        Image bgImage = new ImageIcon(this.getClass().getResource(TITLE_SCREEN_IMAGE))
                .getImage()
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);



        titlePane = new JLabel(new ImageIcon(bgImage));
        titlePane.setSize(WIDTH, HEIGHT);
        titlePane.setLayout(new GridBagLayout());

        // Button
        JButton playButton = new JButton();
        playButton.setText("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 26));
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.setFocusPainted(false);
        playButton.setForeground(Color.WHITE);

        playButton.addActionListener((e) -> {
            frame.setContentPane(introPane);
            introPane.requestFocus();

        });

        // Add the components
        titlePane.add(playButton);

        frame.setContentPane(titlePane);

    }

    public static void main(String[] args) {
        new GameWindow();
    }
}
