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
    private static Controller controller = new Controller(); // make an instance of controller for player commands
    private static Scanner scan = new Scanner(System.in);
    private static List<Room> map;                      // ref to testMap.rooms

    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        controller.loadAnsiConsole();                        // enables Jansi ANSI support

        controller.loadGameObjects();                        // loads all objects used for game logic into memory

        // controller.titleCard();                              // display title card

        // controller.prologue();                               // display prologue text

        // controller.gameInstructions();                      // display game instructions


        Controller.clearConsole();
        remindStatus(); // remind user of status
        // breaking this while loop means the game is over
        while (true) {
            try {
                String userInput = "";                              // empty string to hold user response

                // continues until user enters something
                while (userInput.equals("")) {

                    System.out.println("Enter a Command (HELP for command list): ");        // prompt a user response
                    userInput = scan.nextLine();                    // stop for user data entry
                    userInput = userInput.toLowerCase();            // normalizing input
                    String[] inputArr = userInput.split(" "); // create array for split input

                    Controller.clearConsole();
                    remindStatus(); // remind user of status
                    //--------------------------------------PLAYER COMMANDS--------------------------------------------//
                    controller.userCommands(inputArr);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void remindStatus() {
        Controller.clearConsole();
        // no command input requires showStatus() to display details to user again
        String roomDescription = "";                             // create empty string to hold description
        List<Room> rooms = controller.getRoomsList();            // gets a ref to list of rooms
        for (int i = 0; i < rooms.size(); i++) {                 // search through all rooms for currentRoom description
            if (rooms.get(i).name.equals(controller.getPlayer().getCurrentRoom())) {  // if found...
                roomDescription = rooms.get(i).description;      // ...create string to hold currentRoom's description
            }
        }

        // get status for no input, which usually would not exit the while to display showStatus() again
        controller.showStatus(controller.getPlayer().getCurrentRoom(), roomDescription);
    }

    public static Controller getController() {
        return controller;
    }
}

