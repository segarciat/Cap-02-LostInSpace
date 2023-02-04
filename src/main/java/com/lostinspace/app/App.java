package com.lostinspace.app;

/*
 * Development Main Class |
 * Main game logic for dev purposes.
 * Handles all game logic.
 * Calls Controller for player commands
 */

import com.lostinspace.model.Room;
import com.lostinspace.controller.Controller;

import java.io.*;
import java.util.*;

public class App {

    public static String currentRoom = new String();         // current location of Player
    private static Controller controller = new Controller(); // make an instance of controller for player commands
    private static Scanner scan = new Scanner(System.in);
    private static List<Room> map;                      // ref to testMap.rooms

    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        controller.loadAnsiConsole();                        // enables Jansi ANSI support

        controller.loadGameObjects();                        // loads all objects used for game logic into memory

        controller.titleCard();                              // display title card

        controller.prologue();                               // display prologue text

        controller.gameInstructions();                      // display game instructions

        // breaking this while loop means the game is over
        while (true) {
            String description = "";                            // create empty string to hold description

            map = controller.getRoomsList();                    // gets a ref to list of rooms
            for (int i = 0; i < map.size(); i++) {              // search through all rooms for currentRoom description
                if (map.get(i).name.equals(controller.getPlayer().getCurrentRoom())) {      // if found...
                    description = map.get(i).description;       // ...create string to hold currentRoom's description
                }
            }

            controller.showStatus(controller.getPlayer().getCurrentRoom(), description);    // create an instance of our player controller
            String userInput = "";                              // empty string to hold user response

            // continues until user enters something
            while (userInput.equals("")) {

//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
                System.out.println("Enter a command: ");        // prompt a user response
                userInput = scan.nextLine();                    // stop for user data entry

                userInput = userInput.toLowerCase();            // normalizing input
                String[] inputArr = userInput.split(" "); // create array for split input


                //--------------------------------------PLAYER COMMANDS--------------------------------------------//
                controller.userCommands(inputArr);
            }
        }
    }
}

