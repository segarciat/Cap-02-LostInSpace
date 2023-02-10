package com.lostinspace.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class EnterKeyListener implements KeyListener {
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            System.out.println("'Enter' key pressed");
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}