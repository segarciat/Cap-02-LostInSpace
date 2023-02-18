package com.lostinspace.view;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.Model;
import com.lostinspace.model.Room;
import com.lostinspace.util.SoundLoader;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AppView {
    // size of objects
    private static final int FRAME_WIDTH = 720;
    private static final int FRAME_HEIGHT = 720;

    // titles
    private static final String GAME_TITLE = "Lost In Space";
    public static final String GAME_BACKGROUND_MUSIC_FILE = "sound/space-chillout.wav";
    public static final String WIN_MUSIC = "sound/win.wav";
    public static final String CONSOLE_PUZZLE_TITLE = "Console Puzzle";
    public static final String CONSOLE_PUZZLE_PROMPT = "What are the colors on the poster in the ship?";

    // java swing components
    private final JFrame frame;
    private Map<String, RoomPanel> roomFrames;

    private final TitlePanel titlePanel;
    private final IntroPanel introPanel;
    private final MenuPanel menuPanel;
    private final MapPanel mapPanel;
    private WinPanel winPanel;

    // controllers
    private final GUIController controller;
    private final Model model;
    private RoomPanel currentRoomPanel;

    // Music to play
    private Clip gameMusicClip;
    private int currentVolume = 10;

    // other
    private Route route = Route.TITLE;               // routing section of story

    /*
     * GUI Constructor
     * Initializes the fields, controllers, and JFrame
     */
    public AppView(GUIController controller, Model model) {
        this.controller = controller;
        this.model = model;

        changeLookAndFeel();

        frame = new JFrame();
        setFrameAttributes();

        createRooms();
        titlePanel = new TitlePanel(this);
        menuPanel = new MenuPanel(this);
        introPanel = new IntroPanel(this);
        mapPanel = new MapPanel(this);

        frame.addKeyListener(new KeyToggleAction());
        gameMusicClip = SoundLoader.loadMusic(GAME_BACKGROUND_MUSIC_FILE);
        gameMusicClip.start();
    }

    public void close() {
        frame.dispose();
        gameMusicClip.close();
    }

    /**
     * For Mac user's, the look and feel will match windows format
     */
    private void changeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {

        }
    }

    public List<String> showConsoleGame(List<String> colors) {
        // Create panel to hold prompt questions and checkboxes.
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionPanel.add(new JLabel(UIManager.getIcon("OptionPane.questionIcon")));

        // Add the prompt question.
        JLabel promptLabel = new JLabel(CONSOLE_PUZZLE_PROMPT);
        questionPanel.add(promptLabel);

        // Create panel for the boxes
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(questionPanel);

        // Create checkboxes for each of those colors and add them to the panel.
        List<JCheckBox> checkBoxes = colors.stream().map(JCheckBox::new).collect(Collectors.toList());

        // Add all checkboxes to the panel.
        checkBoxes.forEach(panel::add);

        int result = JOptionPane.showConfirmDialog(frame,
                panel,
                CONSOLE_PUZZLE_TITLE,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        List<String> userResponse = checkBoxes.stream()
                .filter(JCheckBox::isSelected)
                .map(JCheckBox::getText)
                .sorted()
                .collect(Collectors.toList());

        return userResponse;
    }

    /*
     * Set frame attributes
     */
    private void setFrameAttributes() {
        frame.setTitle(GAME_TITLE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);                           // Set frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);               // Closes window and terminates
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
    }

    /*
     * Entry into App GUI
     */
    public void update() {
        switch (route) {
            case TITLE:
                showTitleScreen();
                break;
            case PROLOGUE:
                showPrologue();
                break;
            case GAME:
                showGame();
                break;
            case MENU:
                showMenu();
                break;
            case MAP:
                showMap();
                break;
            case WIN:
                showWinPanel();
                break;
            default:
                break;
        }
    }

    private void showMenu() {
        frame.setContentPane(menuPanel);
        frame.requestFocus();

    }

    private void showMap() {
        frame.setContentPane(mapPanel);
        frame.requestFocus();
    }

    /*
     * Create title frame`
     */
    private void showTitleScreen() {
        frame.setContentPane(titlePanel);
        frame.setVisible(true);
        frame.requestFocus();
    }

    /*
     * Create prologue sequence
     */
    private void showPrologue() {
        frame.setContentPane(introPanel);
        introPanel.allowPageSkipOnKeyPress();
        frame.requestFocus();
    }

    /*
     * Create game
     */
    private void showGame() {
        String currentRoomName = controller.getPlayer().getCurrentRoom();
        currentRoomPanel = roomFrames.get(currentRoomName);

        currentRoomPanel.updateView();

        frame.setContentPane(currentRoomPanel);
        frame.revalidate();
    }

    /*
     * Create win panel
     * Only create instance of win panel when the player enters this route to start the timer for image transitions
     */
    private void showWinPanel() {
        winPanel = new WinPanel(this);
        frame.setContentPane(winPanel);
        frame.requestFocus();
        gameMusicClip.close();
        gameMusicClip = SoundLoader.loadMusic(WIN_MUSIC);
        gameMusicClip.start();
        gameMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        setVolume(currentVolume);
    }

    /*
     * Create objects for the game
     */
    private void createRooms() {
        roomFrames = new HashMap<>();

        Map<String, Room> roomMap = controller.getRoomMap();

        for (String roomName : roomMap.keySet()) {
            Room room = roomMap.get(roomName);
            roomFrames.put(roomName, new RoomPanel(this, room, controller));
        }
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        if (Route.PROLOGUE.equals(this.route) && Route.GAME.equals(route)) {
            gameMusicClip.start();
            gameMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        this.route = route;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Map<String, RoomPanel> getRoomFrames() {
        return Collections.unmodifiableMap(roomFrames);
    }

    public GUIController getController() {
        return controller;
    }

    public RoomPanel getCurrentRoomPanel() {
        return currentRoomPanel;
    }

    /**
     * Allows player to toggle (open/close) the menu when pressing ESC key.
     */
    private class KeyToggleAction implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // if the route is game, show the menu
                if (Route.GAME.equals(route)) {
                    setRoute(Route.MENU);
                    update();
                     gameMusicClip.stop();
                } else if (Route.MENU.equals(route)) {
                    // if the route is menu, it should hide the menu
                    setRoute(Route.GAME);
                    update();
                    gameMusicClip.start();
                }

            } else if (e.getKeyCode() == KeyEvent.VK_M) {
                // if the route is game, show the map
                if (Route.GAME.equals(route)) {
                    setRoute(Route.MAP);
                    update();
                } else if (Route.MAP.equals(route)) {
                    // if the route is map, it should hide the map
                    setRoute(Route.GAME);
                    update();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                // Increase volume
                if (gameMusicClip != null) {
                    if (!(currentVolume++ > 20)) {
                        setVolume(currentVolume++);
                    }
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                // Decrease volume
                if (!(currentVolume-- < 1)) {
                    setVolume(currentVolume--);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT && gameMusicClip.isActive()) {
                gameMusicClip.stop();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !gameMusicClip.isActive()) {
                gameMusicClip.start();
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private void setVolume(int volume) {
        // the actual range is -80.0 to 6.0206 dB. this conversion allows for a range of 1-20 and is adjusted
        float dB = ((float) volume / 20 * 46) - 40;
        if (gameMusicClip != null) {
            FloatControl volumeControl = (FloatControl) gameMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumeControl != null && volumeControl.getMinimum() <= dB && dB <= volumeControl.getMaximum()) {
                volumeControl.setValue(dB);
            }
        }
    }
}
