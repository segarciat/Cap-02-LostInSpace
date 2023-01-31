package com.lostinspace.util;

import java.io.IOException;

public class GameEvents {
    EventThread evThread = new EventThread();   // instance of EventThread logic.
    Thread th = new Thread(evThread);           // create Thread and attach EventThread obj logic
    Controller controller = new Controller();   // instance of Controller methods

    public void enterToContinue() throws IOException {

        System.out.println("\n\nPress ENTER to Continue--->"); // Tell user how to continue
        System.in.read();                                      // listen for keyboard input
        evThread.proceed = true;       // get out of the evThread loop
        controller.clearConsole();     // clear the console
    }
}

class EventThread extends Thread{
    public boolean proceed = false;    // breaks the while loop

    /*
     * loop continues until user presses Enter
     * this continues the logic to next line of
     * enterToContinue() where the loop ends and
     * the game logic may proceed
    */
    public void run(){
        while(proceed){
            if(proceed){ // exit loop when changed
                return;
            }
        }
    }

}
