package com.lostinspace.util;

/*
 * Player Controller Class | Author: Mike Greene
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 * Handles loading game map into memory.
 */

import com.google.gson.Gson;
import com.lostinspace.app.App;
import com.lostinspace.model.Item;
import com.lostinspace.model.PointOfInterest;
import com.lostinspace.model.Room;
import com.lostinspace.model.RoomsRoot;
import org.fusesource.jansi.*;

import org.fusesource.jansi.AnsiConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fusesource.jansi.Ansi.ansi;

/*
 * handles user commands and their feedback
 * stores inventory data
 * loads game text/dialogue
 */
public class Controller {
    private static final String os = System.getProperty("os.name").toLowerCase(); // identify operating system of user
    static FileGetter filegetter = new FileGetter();                              // FileGetter retrieves resources
    static GameEvents events = new GameEvents();                                  // ref to Game Event Methods
    Gson gson = new Gson();                                                       // Gson object converts JSON objects

    // variables for string coloring
    public static final String ANSI_RESET = "\u001B[0m";   // resets the color
    public static final String ANSI_GREEN = "\u001B[32m";  // color values  |
    public static final String ANSI_BLUE = "\u001B[34m";   //               |
    public static final String ANSI_RED = "\u001B[31m";    //               |
    public static final String ANSI_YELLOW = "\u001B[33m"; //               |

    List<String> inventory = Arrays.asList();              // player inventory, which is initially empty   
    static String currentRoom = "Docking Bay";             // current string location of player

    //-------------------------------CONTROLLER METHODS

    public void userCommands(String[] inputArr) throws IOException {
        // SINGLE WORD COMMANDS
        // exit the game
        if(inputArr[0].equals("exit") || inputArr[0].equals("quit") || inputArr[0].equals("escape")){
            quit();
        }

        // restart the game
        else if(inputArr[0].equals("new") || inputArr[0].equals("restart") || inputArr[0].equals("escape")){
            restart();
        }

        // restart the game
        else if(inputArr[0].equals("help") || inputArr[0].equals("instructions")){
            clearConsole();
            gameInstructions();
        }

        // check for commands that are too short or too long
        else if(inputArr.length < 2 || inputArr.length > 2){
            clearConsole();
            System.out.println("You want to "+ inputArr[0] + ", but what exactly?" + "\nYou MUST enter valid command!\n\n(Enter HELP for a list of commands)");
        }
        else if(inputArr.length > 2){
            clearConsole();
            System.out.println("Too many words in your command." + "\nOnly enter VALID 2-WORD commands!\n\n(Enter HELP for a list of commands)");
            events.enterToContinue();
        }

        // MULTI-WORD COMMANDS
        // movement commands
        else if (inputArr[0].equals("go") || inputArr[0].equals("walk") || inputArr[0].equals("move") || inputArr[0].equals("run")) {
            // check that player is allowed to go in that direction
            setCurrentRoom(move(App.getMap(), getCurrentRoom(), inputArr[1]));
        }

        // inspect rooms, items, or anything listed as a Point of interest
        else if(inputArr[0].equals("look") || inputArr[0].equals("inspect") || inputArr[0].equals("examine") || inputArr[0].equals("study") || inputArr[0].equals("investigate")){
            // rooms are inspected differently than items
            if(inputArr[1].equals("room")){
                clearConsole();
                System.out.println(inspectRoom(App.getMap(), getCurrentRoom()));
                events.enterToContinue();
            } else {
                clearConsole();
                System.out.println(inspectItem(App.getMap(), getCurrentRoom(), inputArr[1]));
                events.enterToContinue();
            }
        }

        // todo debug commands, REMOVE upon release
        else if(inputArr[0].equals("output-test")){
            String toSend ="{\"inventory\": [[\"RESURRECTION\"]]}";
            FileSetter fileSetter = new FileSetter();
            fileSetter.saveToFile(toSend);
        }

        // invalid command
        else {
            clearConsole();
            System.out.println("I don't know how to " + inputArr[0] + " anything! Enter a valid COMMAND!");
            events.enterToContinue();
        }
    }

    // Display prologue text
    public static void prologue() {
        String text = ""; // empty return string

        try(Reader data = filegetter.getResource("prologue.txt")) {
            // load file from resources dir
            BufferedReader reader = new BufferedReader(data);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");

            // while true that there are still lines of characters to read
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line);
                sBuilder.append(ls);
            }

            // delete the last new line separator
            sBuilder.deleteCharAt(sBuilder.length() - 1);
            reader.close();
            text = sBuilder.toString();
            String[] lines = text.split(System.getProperty("line.separator"));

            double linesToDisplay = 13;
            double printLimit = lines.length/linesToDisplay;
            printLimit = Math.ceil(printLimit);

            int idx = 0;
            while(printLimit > 0){
                for(int i = 0; i < linesToDisplay; i++){
                    if(!lines[idx].equals(null)){
                        System.out.println(lines[idx]);
                        idx++;
                    }
                    if(idx == lines.length){
                        break;
                    }
                }
                events.enterToContinue();
                printLimit--;
            }

            // throw IO Exception if failed
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // Display game Title Card
    public static void titleCard() throws IOException {
        String content = ""; // empty return string

        try(Reader title = filegetter.getResource("welcome.txt")) {
            // load file from resources dir

            BufferedReader reader = new BufferedReader(title);

            StringBuilder sB = new StringBuilder();            // sB builds title card line by line
            String line = null;                                // empty string for line
            String ls = System.lineSeparator();                // line separator

            // while there are still lines of characters to read
            while ((line = reader.readLine()) != null) {
                sB.append(line);                    // append the next line to the SB
                sB.append(ls);                      // new line
            }

            sB.deleteCharAt(sB.length() - 1);       // delete the last new line separator
            content = sB.toString();                // create new string with sB content
            System.out.println(content);            // display title card!

        } catch (IOException err) {                 // throw IO Exception if failed
            throw new RuntimeException(err);
        }

        System.out.println(content);
        events.enterForNewGame();                  // user must press enter to continue
    }

    // Display user commands
    public static void gameInstructions() throws IOException {
        String instructions = ""; // empty return string

        try(Reader data = filegetter.getResource("instructions.txt")) {
            // load file from resources dir
            BufferedReader reader = new BufferedReader(data);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");

            // while true that there are still lines of characters to read
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line);
                sBuilder.append(ls);
            }
            // delete the last new line separator
            sBuilder.deleteCharAt(sBuilder.length() - 1);
            reader.close();
            instructions = sBuilder.toString();

            System.out.println(instructions);
            events.enterForNewGame();                  // user must press enter to continue

            // throw IO Exception if failed
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

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
        clearConsole();
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
        List<Room> map = mapObj.rooms;

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

    /*
     * allows player to inspect rooms to find items and exits
     * returns string detailing
     */
    public static String inspectRoom(RoomsRoot mapObj, String room) {
        String retDescribe = "You survey the area. \n\nYou're able to find: \n"; // string holds return description
        List<Room> map = mapObj.rooms;                                      // get a list of all rooms in the map

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
        List<Room> map = mapObj.rooms;                             // get a list of all rooms in the map

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


    //-------------------------------UTILITY METHODS

    // uses Jansi ANSI methods to clear terminal and reset cursor at 0,0
    public static void clearConsole() {
        System.out.println( ansi().eraseScreen() );
        System.out.println( ansi().cursor(0,0) );
    }

    // Enables the Jansi ANSI support
    public void loadAnsiConsole(){
        AnsiConsole.systemInstall();
    }

    // returns the game map object, RoomsRoot
    public RoomsRoot loadMap() throws IOException {
        RoomsRoot retText = new RoomsRoot();                                // create empty map object

        try (Reader reader = filegetter.getResource("shiprooms.json")) {  // try with
            retText = gson.fromJson(reader, RoomsRoot.class);                 // Convert JSON File to Java Object
            retText.createMap();

            return retText;                                                 // return game map
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }


    //-------------------------------ACCESSOR METHODS
    public static String getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(String currentRoom) {
        Controller.currentRoom = currentRoom;
    }
}
