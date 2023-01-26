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
    private static RoomsRoot testMap = new RoomsRoot();      // create an instance of the game map
    private static ArrayList<Room> map;                             // ref to testMap.rooms
    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        currentRoom = "Docking Bay";                         // start the player in the Docking Bay
        System.out.println(controller.showInstructions());   // display instructions

        // try to load the game map
        testMap = controller.loadMap();                      // load the map into memory
        map = testMap.rooms;

        // todo .loadMap() tests - DELETE ONCE TEAM HAS APPROVED BRANCH
        System.out.println("Name: " + map.get(1).name + "  Exit: " + map.get(1).exits.getEast());
        System.out.println("Name: " + map.get(0).name + "  Exit: " + map.get(0).exits.getWest());


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

                // PLAYER COMMANDS

                // movement commands
                if (inputArr[0].equals("go") || inputArr[0].equals("walk") || inputArr[0].equals("move") || inputArr[0].equals("run")) {
                    // check that player is allowed to go in that direction
                    currentRoom = move(currentRoom, inputArr[1]);

                }

                // exit the game
                if(inputArr[0].equals("exit") || inputArr[0].equals("quit") || inputArr[0].equals("escape")){
                    System.exit(0);
                }
            }
        }
    }

    // moves player between rooms in map
    // returns string which resets currentRoom
    public static String move(String room, String dir) {
        String retRoom = ""; // create empty string to hold return room

        // iterate through map
        for(int i = 0; i < map.size(); i++){
            // if the direction desired exists as an exit in that room...
            if(map.get(i).name.equals(room)){
                // ...reassign retRoom as the room in that direction
                switch (dir){
                    case "north":
                        retRoom = map.get(i).exits.getNorth();
                        break;

                    case "south":
                        retRoom = map.get(i).exits.getSouth();
                        break;

                    case "east":
                        retRoom = map.get(i).exits.getEast();
                        break;

                    case "west":
                        retRoom = map.get(i).exits.getWest();
                        break;
                    // if an invalid direction is chosen, tell the player
                    default:
                        System.out.println("\nINVALID DIRECTION: " + dir);
                        System.out.println("\nChoose a valid direction. (Hint: INSPECT ROOM if you're lost)");
                        retRoom = room;
                        break;
                }

                // if retRoom is an empty string then there is no exit in that direction
                if(retRoom.equals("")){
                    System.out.println("\nINVALID DIRECTION: " + dir);
                    System.out.println("\nThere is no EXIT in that DIRECTION. (Hint: INSPECT ROOM if you're lost)");
                    retRoom = room;

                    return retRoom; // return back to starting room
                }
            }
        }
        return retRoom; // return new room
    }
}
