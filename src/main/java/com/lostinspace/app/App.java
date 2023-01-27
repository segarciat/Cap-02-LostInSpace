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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class App {
    public static String currentRoom = new String();         // current location of Player
    private static Controller controller = new Controller(); // make an instance of controller for player commands
    private static RoomsRoot testMap = new RoomsRoot();      // create an instance of the game map
    private static ArrayList<Room> map;                             // ref to testMap.rooms
    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        // todo multi-threading for delays in text



        titleCard();                                         // display title card
        gameInstructions();                                  //Provide game instructions
        currentRoom = "Docking Bay";                         // start the player in the Docking Bay
        //System.out.println(controller.showInstructions());   // display instructions

        // try to load the game map
        testMap = controller.loadMap();                      // load the map into memory
        map = testMap.rooms;


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
                    currentRoom = controller.move(testMap, currentRoom, inputArr[1]);


                }

                // exit the game
                if(inputArr[0].equals("exit") || inputArr[0].equals("quit") || inputArr[0].equals("escape")){
                    System.exit(0);
                }

                // restart the game
                if(inputArr[0].equals("new") || inputArr[0].equals("restart") || inputArr[0].equals("escape")){
                    restart();
                }
            }
        }
    }

    // restarts game when called
    public static void restart(){
        String[] string = {};
        try {
            main(string);
        } catch (IOException err){
            err.printStackTrace();
        }
    }



    static String titleCard() {
        String content = ""; // empty return string

        try {
            // load file from resources dir
            BufferedReader reader = new BufferedReader(new FileReader("data/scripts/welcome.txt"));

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

        try {
            // load file from resources dir
            BufferedReader reader = new BufferedReader(new FileReader("data/scripts/instructions.txt"));
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

