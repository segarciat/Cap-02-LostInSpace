package com.lostinspace.app;

/*
 * Development Main Class | Author: Mike Greene
 * Main game logic for dev purposes.
 * Handles all game logic.
 * Calls Controller for player commands
 */

import com.lostinspace.classes.Room;
import com.lostinspace.classes.RoomsRoot;
import com.lostinspace.util.Controller;

import java.io.IOException;
import java.util.*;

public class DevMain {
    public static String currentRoom = new String();         // current location of Player
    private static Controller controller = new Controller(); // make an instance of controller for player commands

    public static void main(String[] args) throws IOException {
        currentRoom = "Docking Bay";                         // start the player in the Docking Bay
        System.out.println(controller.showInstructions());   // display instructions

        RoomsRoot testMap = new RoomsRoot();                 // create an instance of the game map
        ArrayList<Room> map;

        // try to load the game map
        testMap = controller.loadMap();                      // load the map into memory
        map = testMap.rooms;

        // todo .loadMap() tests - DELETE ONCE TEAM HAS APPROVED BRANCH
        System.out.println("Name: " + testMap.rooms.get(1).name + "  Exit: " + testMap.rooms.get(1).exits.getEast());
        System.out.println("Name: " + testMap.rooms.get(0).name + "  Exit: " + testMap.rooms.get(0).exits.getWest());


        // breaking this while loop means the game is over
        while (true) {
            String description = "";                            // create empty string to hold description

            for (int i = 0; i < map.size(); i++) {              // search through all rooms for currentRoom description
                if (map.get(i).name.equals(currentRoom)) {      // if found...
                    description = map.get(i).description;       // ...create string to hold currentRoom's description
                }
            }

            controller.showStatus(currentRoom, description);    // create an instance of our player controller
            String userInput = "";                              // empty string to hold user response


            // continues until user enters something
            while (userInput.equals("")) {
                Scanner scan = new Scanner(System.in);          // create Scanner object for user input

                System.out.println("Enter a command: ");        // prompt a user response
                userInput = scan.nextLine();                    // stop for user data entry

                userInput = userInput.toLowerCase();            // normalizing input
                String[] inputArr = userInput.split(" "); // create array for split input

                // movement commands
                if (inputArr[0].equals("go") || inputArr[0].equals("walk") || inputArr[0].equals("move") || inputArr[0].equals("run")) {
                    // check that player is allowed to go in that direction
                    //controller.move(currentRoom, inputArr[1]);

                }
            }
        }
    }
}
