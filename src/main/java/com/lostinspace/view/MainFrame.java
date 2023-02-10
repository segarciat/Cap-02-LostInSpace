package com.lostinspace.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import javax.swing.*;

class MainFrame extends JFrame implements KeyListener {
    private JPanel panel;
    private MainTextArea textArea;
    private boolean isMovingOn = false;

    private FrameCreator frameCreator;

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String keyword = AppGUI.getGUILocation();
            Iterator<String> iterator = AppGUI.getIterator();

            if (!iterator.hasNext()) {
                isMovingOn = true;
            }

            switch(keyword) {
                case "Prologue":
                    if (isMovingOn) {
                        AppGUI.createInstructions();
                    } else {
                        setTextArea(iterator);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public MainFrame(JPanel panel, MainTextArea textArea){
        addKeyListener(this);
        setFocusable(true);

        this.panel = panel;
        this.textArea = textArea;

        this.frameCreator = new FrameCreator();
    }

    private void setTextArea(Iterator <String> iterator) {
        frameCreator.createSubsequentFullScreenText(textArea, iterator);
    }
}