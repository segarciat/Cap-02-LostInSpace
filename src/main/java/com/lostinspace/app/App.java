package com.lostinspace.app;

/*
 * Development Main Class | Author: Mike Greene
 * Main game logic for dev purposes.
 * Handles all game logic.
 * Calls Controller for player commands
 */

//import com.apps.util.Console;       // todo why cant intellij see this library?
import com.lostinspace.model.Room;
import com.lostinspace.model.RoomsRoot;
import com.lostinspace.util.Controller;
import com.lostinspace.util.FileGetter;
import com.lostinspace.util.FileSetter;
import com.lostinspace.util.GameEvents;

import java.io.*;
import java.util.*;

public class App {
    public static String currentRoom = new String();         // current location of Player
    private static Controller controller = new Controller(); // make an instance of controller for player commands
    private static RoomsRoot testMap = new RoomsRoot();      // create an instance of the game map
    private static ArrayList<Room> map;                      // ref to testMap.rooms
    static FileGetter filegetter = new FileGetter();
    static GameEvents events = new GameEvents();
    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        // todo multi-threading for delays in text
        controller.loadAnsiConsole();                        // enables Jansi ANSI support

        titleCard();                                         // display title card
        events.enterToContinue();                            // user must press enter to continue

        gameInstructions();                                  // display game instructions
        events.enterToContinue();

        currentRoom = "Docking Bay";                         // start the player in the Docking Bay

        // try to load the game map
        testMap = controller.loadMap();                      // load the map into memory
        map = testMap.rooms;                                 // ref to rooms list


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

                //--------------------------------------PLAYER COMMANDS--------------------------------------------//

                // movement commands
                if (inputArr[0].equals("go") || inputArr[0].equals("walk") || inputArr[0].equals("move") || inputArr[0].equals("run")) {
                    // check that player is allowed to go in that direction
                    currentRoom = controller.move(testMap, currentRoom, inputArr[1]);
                }

                // inspect rooms, items, or anything listed as a Point of interest
                else if(inputArr[0].equals("look") || inputArr[0].equals("inspect") || inputArr[0].equals("examine") || inputArr[0].equals("study") || inputArr[0].equals("investigate")){
                    // rooms are inspected differently than items
                    if(inputArr[1].equals("room")){
                        System.out.println(controller.inspectRoom(testMap, currentRoom, inputArr[1]));
                    } else {
                        System.out.println(controller.inspectItem(testMap, currentRoom, inputArr[1]));
                    }
                }

                // exit the game
                else if(inputArr[0].equals("exit") || inputArr[0].equals("quit") || inputArr[0].equals("escape")){
                    controller.quit();
                }

                // restart the game
                else if(inputArr[0].equals("new") || inputArr[0].equals("restart") || inputArr[0].equals("escape")){
                    controller.restart();
                }

                // todo debug commands, REMOVE upon release
                else if(inputArr[0].equals("output-test")){
                    String toSend ="{\"inventory\": [[\"RESURRECTION\"]]}";
                    FileSetter fileSetter = new FileSetter();
                    fileSetter.saveToFile(toSend);
                }

                else {
                    System.out.println("I don't know how to " + inputArr[0] + " anything! Enter a valid COMMAND!");
                }
            }
        }
    }

    static String titleCard() {
        String content = ""; // empty return string

        try(Reader title = filegetter.getResource("welcome.txt")) {
            // load file from resources dir

            BufferedReader reader = new BufferedReader(title);

            StringBuilder sB = new StringBuilder();            // sB builds title card line by line
            String line = null;                                // empty string for line
            String ls = System.getProperty("line.separator");  // line separator

            // while there are still lines of characters to read
            while ((line = reader.readLine()) != null) {
                sB.append(line);                    // append the next line to the SB
                sB.append(ls);                      // new line
            }

            sB.deleteCharAt(sB.length() - 1);       // delete the last new line separator
            reader.close();                         // close file being worked with
            content = sB.toString();         // create new string with sB content
            System.out.println(content);            // display title card!

        } catch (IOException err) {                 // throw IO Exception if failed
            err.printStackTrace();
        }

        return content;
    }

    static String gameInstructions() {
        String Instructions = ""; // empty return string

        try(Reader instructions = filegetter.getResource("instructions.txt")) {
            // load file from resources dir
            BufferedReader reader = new BufferedReader(instructions);
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
            Instructions = sBuilder.toString();
            Instructions.format("\nPress enter to proceed.\n");
            //Instructions.nextLine();
            System.out.println(Instructions);
            // throw IO Exception if failed
        } catch (IOException err) {
            err.printStackTrace();
        }
        return Instructions;
    }
}

