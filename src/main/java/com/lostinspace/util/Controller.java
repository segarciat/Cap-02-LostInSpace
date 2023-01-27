package com.lostinspace.util;

/*
 * Player Controller Class | Author: Mike Greene
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 * Handles loading game map into memory.
 */

import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import com.google.gson.Gson;
import com.lostinspace.classes.Room;
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

    /*
     * determine the current status of the player
     * displays current location, inventory, and Oxygen levels
     */
    public void showStatus(String location, String description) {


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

    // moves player between rooms in map
    // returns string which resets currentRoom
    public static String move(RoomsRoot mapObj, String room, String dir) {
        String retRoom = ""; // create empty string to hold return room
        ArrayList<Room> map = mapObj.rooms;

        // iterate through map
        for(int i = 0; i < map.size(); i++){
            // if the direction desired exists as an exit in that room...
            if(map.get(i).getName().equals(room)){
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

    /* Todo fix Controller.clearConsole to clear terminal between commands
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
    }*/

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

//    #if they type 'go' first
//    if move[0] == 'go':
//            #check that they are allowed wherever they want to go
//        if move[1] in rooms[currentRoom]:
//            #set the current room to the new room
//            currentRoom = rooms[currentRoom][move[1]]
//        # if they aren't allowed to go that way:
//            else:
//    print(crayons.red('You can\'t go that way!', bold=True))
//            time.sleep(1)
//
//            #if they type 'get' first
//    if move[0] == 'get' :
//            # make two checks:
//            # 1. if the current room contains an item
//        # 2. if the item in the room matches the item the player wishes to get
//        if "item" in rooms[currentRoom] and move[1] in rooms[currentRoom]['item']:
//            #add the item to their inventory
//            inventory.append(move[1])
//            #display a helpful message
//    print(f'\nYou got the {crayons.magenta(move[1])}!\n\n')
//    input(crayons.blue("Press ENTER to continue"))
//            #delete the item key:value pair from the room's dictionary
//    del rooms[currentRoom]['item']
//            # if there's no item in the room or the item doesn't match
//        else:
//                #tell them they can't get it
//    print('Can\'t get ' + move[1] + '!')
//
//    ## If a player enters a room with a monster
//    if 'item' in rooms[currentRoom] and 'monster' in rooms[currentRoom]['item']:
//    print('A monster has got you... GAME OVER!')
//        break
//
//                ## Define how a player can win
//    if currentRoom == 'Garden' and 'key' in inventory and 'potion' in inventory:
//    print('You escaped the house with the ultra rare key and magic potion... YOU WIN!')
//        break
}
