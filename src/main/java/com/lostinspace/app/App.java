package com.lostinspace.app;

/*
 * Development Main Class | Author: Mike Greene
 * Main game logic for dev purposes.
 * Handles all game logic.
 * Calls Controller for player commands
 */

import com.lostinspace.model.Room;
import com.lostinspace.model.RoomsRoot;
import com.lostinspace.util.Controller;

import java.io.*;
import java.util.*;

public class App {
    private static final Controller controller = new Controller(); // make an instance of controller for player commands
    private static RoomsRoot testMap = new RoomsRoot();            // create an instance of the game map
    private static ArrayList<Room> map;                            // ref to testMap.rooms


    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        controller.loadAnsiConsole();                        // enables Jansi ANSI support

        controller.titleCard();                              // display title card

        controller.prologue();                               // display prologue text

        controller.gameInstructions();                      // display game instructions

        // try to load the game map
        testMap = controller.loadMap();                      // load the map into memory
        map = testMap.rooms;                                 // ref to rooms list


        // breaking this while loop means the game is over
        while (true) {
            String description = "";                            // create empty string to hold description

            for (int i = 0; i < map.size(); i++) {              // search through all rooms for currentRoom description
                if (map.get(i).name.equals(controller.getCurrentRoom())) {      // if found...
                    description = map.get(i).description;       // ...create string to hold currentRoom's description
                }
            }

            controller.showStatus(controller.getCurrentRoom(), description);    // create an instance of our player controller
            String userInput = "";                              // empty string to hold user response


            // continues until user enters something
            while (userInput.equals("")) {
                Scanner scan = new Scanner(System.in);          // create Scanner object for user input

                System.out.println("Enter a command: ");        // prompt a user response
                userInput = scan.nextLine();                    // stop for user data entry

                userInput = userInput.toLowerCase();            // normalizing input
                String[] inputArr = userInput.split(" "); // create array for split input


                //--------------------------------------PLAYER COMMANDS--------------------------------------------//
                controller.userCommands(inputArr);
            }
        }
    }

    //ACCESSOR METHODS
    public static RoomsRoot getMap() {
        return testMap;
    }
}

