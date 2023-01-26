package com.lostinspace.util;

/*
 * Player Controller Class | Author: Mike Greene
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 * Handles loading game map into memory.
 */

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import com.google.gson.Gson;
import com.lostinspace.classes.RoomsRoot;

/*
 * handles user commands and their feedback
 * stores inventory data
 * loads game text/dialogue
 */
public class Controller {
    Gson gson = new Gson();                                // creates a new Gson object for converting JSON objects

    // variables for string coloring
    public static final String ANSI_RESET = "\u001B[0m";   // resets the color
    public static final String ANSI_GREEN = "\u001B[32m";  // color values  |
    public static final String ANSI_BLUE = "\u001B[34m";   //               |
    public static final String ANSI_RED = "\u001B[31m";    //               |
    public static final String ANSI_YELLOW = "\u001B[33m"; //               |

    List<String> inventory = Arrays.asList();              // player inventory, which is initially empty

    // displays general instructions to player as a reminder
    public String showInstructions() {

        // returns all commands as long string
        return new StringBuilder()
                .append("COMMANDS:\n\n")
                .append("*go/walk/move[direction] - <move> in selected <direction>\n")
                .append("directions: North, South, East, West\n\n")
                .append("*get/take/grab[item] - add <item> to <inventory>\n")
                .append("item: <inspect> rooms to find <items>\n\n")
                .append("*check[inventory, oxygen] - look at the <item>(s) being held in your <inventory>\n")
                .append("Note: remember to \"CHECK OXYGEN\" often as reaching 0% will END YOUR GAME! \n\n")
                .append("*use[item] - <use> an item in your <inventory> or in the same <room> as you\n")
                .append("Not all items can be used at all times or in every room. Experiment with your options!\n\n")
                .append("*inspect/look/examine/search [room, item, object] - receive a description of what was inspected, look inside containers\n")
                .append("<inspect> will often reveal details about something you are confused about")
                .append("*radio[name] - <radio> your crew to receive their status and helpful hints\n")
                .append("name: <Douglas>, <Zhang> \n\n")
                .append("*objectives - review current game objectives\n\n")
                .append("*new[game] - restart the game\n\n")
                .append("*quit[game] - quits the current game.\n\n")
                .toString();
    }

    /*
     * determine the current status of the player
     * displays current location, inventory, and Oxygen levels
     */
    public void showStatus(String location, String description) {
        //clear text from terminal
        clearConsole();

        System.out.println(ANSI_YELLOW + "---------------------------" + ANSI_RESET);

        System.out.println("You are in the " + location + '\n');            //print the player 's current location

        //todo Add Room Description to showStatus()
        System.out.println(ANSI_GREEN + description + ANSI_RESET);          // print description of current room

        String result = String.join(",", inventory);                // print what the player is carrying
        System.out.println(ANSI_BLUE + String.format("\nInventory: %s", result) + ANSI_RESET);

        // print remaining oxygen
        System.out.println(ANSI_RED + String.format("\nOxygen Level: %f percent", 45.5) + ANSI_RESET);

        System.out.println(ANSI_YELLOW + "---------------------------" + ANSI_RESET);
    }

    // Todo fix Controller.clearConsole to clear terminal between commands
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
//            if (os.contains("Windows")) {
//                Runtime.getRuntime().exec("cls");
//            } else {
            Runtime.getRuntime().exec("clear");
//            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public RoomsRoot loadMap() throws IOException {
        RoomsRoot retText = new RoomsRoot();                                // create empty map object
        try {
            Reader reader = new FileReader("data/sampleText.json"); // read map data file
            retText = gson.fromJson(reader, RoomsRoot.class);               // Convert JSON File to Java Object
        } catch (IOException err) {                                         // throw IO Exception if failed
            err.printStackTrace();
        }

        return retText; // return game map
    }

    public String move(String room, String dir) {
//        for(){
//
//        }
        return "";
    }
}
