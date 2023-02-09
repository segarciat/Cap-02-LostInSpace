package com.lostinspace.app;

/*
 * Development Main Class |
 * Main game logic for dev purposes.
 * Handles all game logic.
 * Calls Controller for player commands
 */

import com.lostinspace.model.HiddenItem;
import com.lostinspace.model.Item;
import com.lostinspace.model.Player;
import com.lostinspace.model.Room;
import com.lostinspace.controller.Controller;
import com.lostinspace.util.Color;
import com.lostinspace.util.TextPrinter;

import java.io.*;
import java.util.*;

public class App {
    public static final String INSTANT_GAME_OVER_ROOM = "Enviro-Field";
    private static Controller controller;
    private static Scanner scan = new Scanner(System.in);
    private static List<Room> map;                      // ref to testMap.rooms

    // GAME LOGIC
    public static void main(String[] args) throws IOException {
        controller = new Controller();                       // make an instance of controller for player commands
        controller.loadGameObjects();                        // loads all objects used for game logic into memory

        controller.titleCard();                              // display title card

        controller.prologue();                               // display prologue text

        controller.gameInstructions();                      // display game instructions

        // Once this ship item is "used", player wins.
        Item useShipInteractable = controller.getInteractables().stream()
                .filter(i -> i.getName().equalsIgnoreCase("ship"))
                .findFirst()
                .get();

        Player player = controller.getPlayer();

        // Uncomment the line below to immediately obtain all items needed to win.
        // satisfyAllWinningConditions();


        Controller.clearConsole();
        remindStatus(); // remind user of status
        // breaking this while loop means the game is over
        while (true) {
            if (useShipInteractable.isUsed()) {
                // Exits the loop and entire application.
                winGameAndExit();
                break;
            } else if (!player.hasOxygen() || player.getCurrentRoom().equalsIgnoreCase(INSTANT_GAME_OVER_ROOM)) {
                loseGameAndExit();
                break;
            }
            try {
                String userInput = "";                              // empty string to hold user response

                // continues until user enters something
                while (userInput.equals("")) {

                    System.out.println("Enter a Command (HELP for command list): ");        // prompt a user response
                    userInput = scan.nextLine().trim().toLowerCase();                       // stop for user data entry and normalize input
                    String[] inputArr = userInput.split(" "); // create array for split input

                    Controller.clearConsole();

                    //--------------------------------------PLAYER COMMANDS--------------------------------------------//
                    controller.userCommands(inputArr);
                    remindStatus(); // remind user of status
                }
            } catch (Exception e) {
                TextPrinter.displayText(e.getCause().getMessage(), Color.RED);
                System.out.println("Exception was thrown");
                remindStatus();
            }
        }
    }

    private static void loseGameAndExit() {
        Player player = controller.getPlayer();
        if (!player.hasOxygen()) {
            TextPrinter.displayText("You've run out of oxygen! Game over.", Color.RED);
        } else if (player.getCurrentRoom().equalsIgnoreCase(INSTANT_GAME_OVER_ROOM)) {
            TextPrinter.displayText(
                    String.format("You entered the %s and immediately vaporized. Game over.", INSTANT_GAME_OVER_ROOM),
                    Color.RED
            );
        }
    }

    private static void winGameAndExit() {
        TextPrinter.displayText("WIN! The ship has been fixed. Time to fly off...", Color.GREEN);
    }

    /**
     * Places necessary items in player inventory and automatically "uses" them.
     * From here, all that's needed to win is to type "use ship".
     *
     */
    private static void satisfyAllWinningConditions() {
        List<Item> inventory = controller.getInventory();
        List<HiddenItem> hiddenItems = controller.getHiddenItems();
        for (HiddenItem hiddenItem: hiddenItems) {
            switch (hiddenItem.getName().toLowerCase()) {
                case "manual":
                case "tool":
                case "component":
                    hiddenItem.setUsed(true);
                    inventory.add(new Item(hiddenItem));
            }
        }
    }

    static void remindStatus() {
        // no command input requires showStatus() to display details to user again
        String roomDescription = "";                             // create empty string to hold description
        Map<String, Room> rooms = controller.getRoomMap();            // gets a ref to list of rooms
        for (Room room : rooms.values()) {                 // search through all rooms for currentRoom description
            if (room.getName().equals(controller.getPlayer().getCurrentRoom())) {  // if found...
                roomDescription = room.getDescription();      // ...create string to hold currentRoom's description
            }
        }

        // get status for no input, which usually would not exit the while to display showStatus() again
        controller.showStatus(controller.getPlayer().getCurrentRoom(), roomDescription);
    }

    public static Controller getController() {
        return controller;
    }
}

