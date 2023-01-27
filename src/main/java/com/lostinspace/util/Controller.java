package com.lostinspace.util;

/*
 * Player Controller Class | Author: Mike Greene
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 * Handles loading game map into memory.
 */

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.lostinspace.app.App;
import com.lostinspace.model.Item;
import com.lostinspace.model.PointOfInterest;
import com.lostinspace.model.Room;
import com.lostinspace.model.RoomsRoot;

/*
 * handles user commands and their feedback
 * stores inventory data
 * loads game text/dialogue
 */
public class Controller {
    Gson gson = new Gson();                                // creates a new Gson object for converting JSON objects
    FileGetter filegetter = new FileGetter();

    // variables for string coloring
    public static final String ANSI_RESET = "\u001B[0m";   // resets the color
    public static final String ANSI_GREEN = "\u001B[32m";  // color values  |
    public static final String ANSI_BLUE = "\u001B[34m";   //               |
    public static final String ANSI_RED = "\u001B[31m";    //               |
    public static final String ANSI_YELLOW = "\u001B[33m"; //               |

    List<String> inventory = Arrays.asList();              // player inventory, which is initially empty   

    // restarts game when called
    public static void restart() {
        String[] string = {};
        try {
            App.main(string);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // quits the game when called
    public static void quit() {
        System.exit(0);
    }


    /*
     * displays the current status of the player, including
     * current location, inventory, and oxygen levels
     */
    public void showStatus(String location, String description) {


        System.out.println(ANSI_YELLOW + "---------------------------" + ANSI_RESET);

        System.out.println("You are in the " + location + '\n');            //print the player 's current location

        System.out.println(ANSI_GREEN + description + ANSI_RESET);          // print description of current room

        String result = String.join(",", inventory);                // print what the player is carrying
        System.out.println(ANSI_BLUE + String.format("\nInventory: %s", result) + ANSI_RESET);

        // print remaining oxygen
        System.out.println(ANSI_RED + String.format("\nOxygen Level: %f percent", 45.5) + ANSI_RESET);

        System.out.println(ANSI_YELLOW + "---------------------------" + ANSI_RESET);
    }

    /*
     * moves player between rooms in map by finding valid exits
     * prompts player to INSPECT ROOM when invalid choice is given.
     * returns string which resets currentRoom in App
     */
    public static String move(RoomsRoot mapObj, String room, String dir) {
        String retRoom = ""; // create empty string to hold return room
        ArrayList<Room> map = mapObj.rooms;

        // iterate through map
        for (int i = 0; i < map.size(); i++) {
            // if the direction desired exists as an exit in that room...
            if (map.get(i).getName().equals(room)) {
                // ...then reassign return room as the room in that direction
                switch (dir) {
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
                if (retRoom.equals("")) {
                    System.out.println("\nINVALID DIRECTION: " + dir);
                    System.out.println("\nThere is no EXIT in that DIRECTION. (Hint: INSPECT ROOM if you're lost)");
                    retRoom = room;

                    return retRoom; // return back to starting room
                }
            }
        }
        return retRoom; // return new room
    }


    /* Todo fix Controller.clearConsole to clear terminal between commands

    /*
     * allows player to inspect rooms to find items and exits
     * returns string detailing
     */
    public static String inspectRoom(RoomsRoot mapObj, String room, String toBeInspected) {
        String retDescribe = "You survey the area. \n\nYou're able to find: \n"; // string holds return description
        ArrayList<Room> map = mapObj.rooms;                                      // get a list of all rooms in the map

        // iterate through room list
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).getName().equals(room)) {                  // find the room object the player is currently in
                ArrayList<Item> items = map.get(i).items;             // quick reference to items list for currentRoom

                // iterate through item list
                for (int j = 0; j < map.get(i).items.size(); j++) {
                    retDescribe = retDescribe + "- " + items.get(j).getFullName() + "\n"; // first add all items to return
                }

                retDescribe = retDescribe + "\nExits: \n";            // then add a header for exits from the room

                // add each existing exit to the return string
                if (!map.get(i).exits.getNorth().equals("")) {          // ignore non-exits
                    retDescribe = retDescribe + "- North: " + map.get(i).exits.getNorth() + "\n";
                }
                if (!map.get(i).exits.getSouth().equals("")) {
                    retDescribe = retDescribe + "- South: " + map.get(i).exits.getSouth() + "\n";
                }
                if (!map.get(i).exits.getEast().equals("")) {
                    retDescribe = retDescribe + "- East: " + map.get(i).exits.getEast() + "\n";
                }
                if (!map.get(i).exits.getWest().equals("")) {
                    retDescribe = retDescribe + "- West: " + map.get(i).exits.getWest() + "\n";
                }

                retDescribe = retDescribe + "\n"; // add a new line for formatting
            }
        }
        return retDescribe;                       // return description
    }

    /*
     * allows player to inspect items and pointsOfInterest
     * returns string detailing what was inspected
     */
    public static String inspectItem(RoomsRoot mapObj, String room, String toBeInspected) {
        String retDescribe = "I cannot INSPECT " + toBeInspected + "!"; // create empty string to hold return description
        ArrayList<Room> map = mapObj.rooms;                             // get a list of all rooms in the map

        // iterate through room list
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).getName().equals(room)) {                // find the room object the player is currently in
                ArrayList<Item> items = map.get(i).items;           // quick reference to items list for currentRoom

                // iterate through item list
                for (int j = 0; j < map.get(i).items.size(); j++) {
                    if (items.get(j).getName().equals(toBeInspected)) {         // see if item to be inspected is in room
                        ArrayList<PointOfInterest> pois = map.get(i).getPois(); // reference to poi list for currentRoom

                        // iterate through list
                        for (int k = 0; k < pois.size(); k++) {
                            if (pois.get(k).getName().equals(toBeInspected)) {  // find item to be inspected in poi list
                                if (pois.get(k).isUsed() == false) {            // if the item has not been used yet
                                    retDescribe = pois.get(k).getDescription(); // return the unused description
                                } else {
                                    retDescribe = pois.get(k).getUsedDescr();   // if it has return the used description
                                }
                            }
                        }
                    }
                }
            }
        }

        return retDescribe; // return description
    }

    // Todo fix Controller.clearConsole to clear terminal between commands

    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
            Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public RoomsRoot loadMap() throws IOException {
        RoomsRoot retText = new RoomsRoot();                               // create empty map object

        try(Reader map = filegetter.getResource("sampleText.json")) {  // try with
            retText = gson.fromJson(map, RoomsRoot.class);                 // Convert JSON File to Java Object
            return retText;                                                // return game map
        }catch(IOException err){
            throw new RuntimeException(err);
        }
    }
}
