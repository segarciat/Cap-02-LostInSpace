package com.lostinspace.controller;

/*
 * Player Controller Class | Author: Mike Greene
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 * Handles loading game map into memory.
 */

import com.google.gson.Gson;
import com.lostinspace.app.App;
import com.lostinspace.model.*;
import com.lostinspace.util.FileGetter;
import com.lostinspace.util.GameEvents;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.Color.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.fusesource.jansi.Ansi.ansi;

/*
 * handles user commands and their feedback
 * stores inventory data
 * loads game text/dialogue
 */
public class Controller {
    private final String os = System.getProperty("os.name").toLowerCase(); // identify operating system of user
    FileGetter filegetter = new FileGetter();       // FileGetter retrieves resources
    GameEvents events = new GameEvents();           // ref to Game Event Methods
    private Gson gson = new Gson();                 // Gson object converts JSON objects
    private List roomsList;                         // import instance of game map from shipRooms.json (game features 16 distinct areas)
    private List items;                             // import instance of list of collectable items
    private List hiddenItems;                       // import instance of list of items that begin as hidden
    private List interactables;                     // import instance of list of interactable objects
    private Map<String, String> itemUses;           // map containing descriptions of item use results

    // create player
    private Player player = new Player("Docking Bay", 80.00);

    List<Item> inventory = Arrays.asList();         // player inventory, which is initially empty

    // todo for testing delete when finished
    public void main(String[] args) {
        Controller controller = new Controller();
        try {
            controller.loadGameObjects();
        } catch (IOException err) {
            throw new RuntimeException(err);
        }

        System.out.println(controller.getItems().get(0).getClass());

    }


    //-------------------------------CONTROLLER METHODS

    // Display prologue text
    public void prologue() {
        String text = ""; // empty return string

        try (Reader data = filegetter.getResource("prologue.txt")) {
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
            double printLimit = lines.length / linesToDisplay;
            printLimit = Math.ceil(printLimit);

            int idx = 0;
            while (printLimit > 0) {
                for (int i = 0; i < linesToDisplay; i++) {
                    if (!lines[idx].equals(null)) {
                        System.out.println(lines[idx]);
                        idx++;
                    }
                    if (idx == lines.length) {
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
    public void titleCard() throws IOException {
        String content = ""; // empty return string

        try (Reader title = filegetter.getResource("welcome.txt")) {
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
    public void gameInstructions() {
        String instructions = ""; // empty return string

        try (Reader data = filegetter.getResource("tutorialText.txt")) {
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


    //--------------------------------PLAYER METHODS

    // commands the player may enter into the console
    public void userCommands(String[] inputArr) throws IOException {
        // SINGLE WORD COMMANDS
        // exit the game
        if (inputArr[0].equals("exit") || inputArr[0].equals("quit") || inputArr[0].equals("escape")) {
            quit();
        }

        // restart the game
        else if (inputArr[0].equals("new") || inputArr[0].equals("restart") || inputArr[0].equals("escape")) {
            restart();
        }

        // restart the game
        else if (inputArr[0].equals("help") || inputArr[0].equals("instructions")) {
            clearConsole();
            help();
        }

        // check for commands that are too short or too long
        else if (inputArr.length < 2 || inputArr.length > 2) {
            clearConsole();
            System.out.println("You want to " + inputArr[0] + ", but what exactly?" + "\nYou MUST enter valid command!\n\n(Enter HELP for a list of commands)");
        } else if (inputArr.length > 2) {
            clearConsole();
            System.out.println("Too many words in your command." + "\nOnly enter VALID 2-WORD commands!\n\n(Enter HELP for a list of commands)");
            events.enterToContinue();
        }

        // MULTI-WORD COMMANDS
        // movement commands
        else if (inputArr[0].equals("go") || inputArr[0].equals("walk") || inputArr[0].equals("move") || inputArr[0].equals("run")) {
            // check that player is allowed to go in that direction
            player.setCurrentRoom(move(getRoomsList(), player.getCurrentRoom(), inputArr[1]));
        }

        // inspect rooms, items, or anything listed as a Point of interest
        else if (inputArr[0].equals("look") || inputArr[0].equals("inspect") || inputArr[0].equals("examine") || inputArr[0].equals("study") || inputArr[0].equals("investigate")) {
            // rooms are inspected differently than items
            if (inputArr[1].equals("room")) {
                clearConsole();
                System.out.println(inspectRoom(getItems(), getInteractables(), getRoomsList(), player.getCurrentRoom()));
                events.enterToContinue();
            } else {
                clearConsole();
                System.out.println(inspectItem(getItems(), getInteractables(), player.getCurrentRoom(), inputArr[1]));
                events.enterToContinue();
            }
        }

        // using items and interactables
        else if (inputArr[0].equals("use")) {
            // check that player is allowed to use the item, then display the results
            clearConsole();
            System.out.println(useItem(getInventory(), getInteractables(), inputArr[1]));
            events.enterToContinue();
        }

        // invalid command
        else {
            clearConsole();
            System.out.println("I don't know how to " + inputArr[0] + " anything! Enter a valid COMMAND!\n\n(Enter HELP for a list of commands)");
            events.enterToContinue();
        }
    }

    // Display commands reminder
    public void help() {
        String instructions = ""; // empty return string

        try (Reader data = filegetter.getResource("instructions.txt")) {
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
            events.enterToContinue();                  // user must press enter to continue

            // throw IO Exception if failed
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // restarts game when called
    public void restart() {
        String[] string = {};
        try {
            App.main(string);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // quits the game when called
    public void quit() {
        System.exit(0);
    }

    /*
     * displays the current status of the player, including
     * current location, inventory, and oxygen levels
     */
    public void showStatus(String location, String description) {
        clearConsole();
        System.out.println(ansi().fg(YELLOW).a("--------------------------------").reset());

        System.out.println("You are in the " + location + '\n');            //print the player 's current location

        System.out.println(ansi().fg(GREEN).a(description).reset());          // print description of current room

        String itemsInInventory = "";  // make empty string to hold item names

        //iterate through the inventory and add each item to the string
        for(int i = 0; i < getInventory().size(); i++) {
            itemsInInventory += " - " + getInventory().get(i).getName();
        }

        // print what the player is carrying
        System.out.println(ansi().fg(BLUE).a(String.format("\nInventory: %s", itemsInInventory)).reset());

        // round oxygen percentage down to 2 decimal places
        double roundOff = Math.round(player.getOxygen()*100)/100;

        // print remaining oxygen
        System.out.println(ansi().fg(RED).a(String.format("\nOxygen Level: %.2f" +
                " percent", roundOff)).reset());

        System.out.println(ansi().fg(YELLOW).a("--------------------------------").reset());
    }

    /*
     * moves player between rooms in map by finding valid exits
     * prompts player to INSPECT ROOM when invalid choice is given.
     * returns string which resets currentRoom in App
     */
    public String move(List<Room> map, String room, String dir) {
        String retRoom = ""; // create empty string to hold return room

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
    public String inspectRoom(List<Item> items, List<Item> interactables, List<Room> rooms, String room) {
        String roomDescription = "You survey the area. \n\nYou're able to find: \n"; // string holds return description
        System.out.println("Current Room: " + room);
        // iterate through room list
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.get(i).getRoom().size(); j++) {
                if (items.get(i).getRoom().get(j).equals(room)) {              // ensure item is in same room as player
                    // first add all items to return
                    roomDescription = roomDescription + "- " + items.get(i).getFullName() + "\n";
                }
            }
        }

        for (int i = 0; i < interactables.size(); i++) {
            for (int j = 0; j < interactables.get(i).getRoom().size(); j++) {
                if (interactables.get(i).getRoom().get(j).equals(room)) {              // ensure item is in same room as player
                    // first add all items to return
                    roomDescription = roomDescription + "- " + interactables.get(i).getFullName() + "\n";
                }
            }
        }

        roomDescription = roomDescription + "\nExits: \n";            // then add a header for exits from the room

        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getName().equals(room)) {
                // add each existing exit to the return string
                if (!rooms.get(i).exits.getNorth().equals("")) {          // ignore non-exits
                    roomDescription = roomDescription + "- North: " + rooms.get(i).exits.getNorth() + "\n";
                }
                if (!rooms.get(i).exits.getSouth().equals("")) {
                    roomDescription = roomDescription + "- South: " + rooms.get(i).exits.getSouth() + "\n";
                }
                if (!rooms.get(i).exits.getEast().equals("")) {
                    roomDescription = roomDescription + "- East: " + rooms.get(i).exits.getEast() + "\n";
                }
                if (!rooms.get(i).exits.getWest().equals("")) {
                    roomDescription = roomDescription + "- West: " + rooms.get(i).exits.getWest() + "\n";
                }

                roomDescription = roomDescription + "\n"; // add a new line for formatting
            }
        }


        return roomDescription;                       // return description
    }

    /*
     * allows player to inspect items and pointsOfInterest
     * returns string detailing what was inspected
     */
    public String inspectItem(List<Item> items, List<Item> interactables, String room, String toBeInspected) {
        String itemDescription = "I cannot INSPECT " + toBeInspected + "!"; // create empty string to hold return description

        // iterate through items list
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equals(toBeInspected) || items.get(i).getSynonyms().contains(toBeInspected)) {  // find the items matching the inspected item
                if (items.get(i).getRoom().contains(room)) {            // if instance of item in the same room
                    if (!items.get(i).isUsed()) {
                        return items.get(i).getDescription();     // then return the unused description
                    } else {
                        return items.get(i).getUsedDescription();   // if it has return the used description
                    }
                }
            } else {
                itemDescription = "There is no \"" + toBeInspected + "\".\n\n(Use INSPECT ROOM if you are looking for an item!)";
            }
        }

        // iterate through interactables list
        for (int i = 0; i < interactables.size(); i++) {
            if (interactables.get(i).getName().equals(toBeInspected) || interactables.get(i).getSynonyms().contains(toBeInspected)) {  // find the interactables matching the inspected item
                if (interactables.get(i).getRoom().contains(room)) {            // if instance of interactable in the same room
                    if (!interactables.get(i).isUsed()) {
                        return interactables.get(i).getDescription();     // then return the unused description
                    } else {
                        return interactables.get(i).getUsedDescription();   // if it has return the used description
                    }
                }
            } else {
                itemDescription = "There is no \"" + toBeInspected + "\".\n\n(Use INSPECT ROOM if you are looking for an item!)";
            }
        }
        return itemDescription; // return description
    }

    /*
     * allows player to use items and pointsOfInterest
     * returns string detailing what was the result
     */
    public String useItem(List<Item> inventory, List<Item> interactables, String toBeUsed) {
        // default return message
        String useDescription = "You're not carrying a  \"" + toBeUsed + "\" right now, nor can you see one.\n\n(Use CHECK INVENTORY or LOOK ROOM if you are looking for an ITEM to use!)";

        // this allows one to retrieve any method using reflection
//        @SuppressWarnings("unchecked") Class<Controller> clazz = controller.getClass();
//        Method method = clazz.getMethod(toBeUsed);
//        method.invoke(controller);

        // iterate through inventory list
        for (int i = 0; i < inventory.size(); i++) {
            // if the item toBeUsed is in the inventory
            if (inventory.get(i).getName().equals(toBeUsed) || inventory.get(i).getSynonyms().contains(toBeUsed)) {
                if(inventory.get(i).getRoom().equals(player.getCurrentRoom())){ // check if the item is in the same room
                    useDescription = getItemUses().get(inventory.get(i).getName());        // find description in item use map by key
                }
            }
        }

        // iterate through interactables list
        for (int i = 0; i < interactables.size(); i++) {
            // if the item toBeUsed is an interactable
            if (interactables.get(i).getName().equals(toBeUsed) || interactables.get(i).getSynonyms().contains(toBeUsed)) {
                if(interactables.get(i).getRoom().contains(player.getCurrentRoom())){ // check if the item is in the same room
                    useDescription = getItemUses().get(interactables.get(i).getName());        // find description in item use map by key
                }
            }
        }

        return useDescription; // return description of what happened
    }


    //-------------------------------UTILITY METHODS

    // uses Jansi ANSI methods to clear terminal and reset cursor at 0,0
    public static void clearConsole() {
        System.out.println(ansi().eraseScreen());
        System.out.println(ansi().cursor(0, 0));
    }

    // Enables the Jansi ANSI support
    public void loadAnsiConsole() {
        AnsiConsole.systemInstall();
    }

    // returns the items list object
    public void loadGameObjects() throws IOException {
        setRoomsList(loadMap().getRooms());                      // load the rooms list into memory
        setItems(loadItems().getItems());                        // load the items list into memory
        setHiddenItems(loadHiddenItems().getHiddenItems());      // load the hidden items list into memory
        setInteractables(loadIteractables().getInteractables()); // load the interactables list into memory
        setItemUses(loadItemUseMap().getItemUseMap());           // load the item use map into memory
    }

    // returns the game map object, RoomsRoot
    public RoomsRoot loadMap() {
        RoomsRoot retText = new RoomsRoot();                                // create empty map object

        try (Reader reader = filegetter.getResource("shiprooms.json")) {  // try with
            retText = gson.fromJson(reader, RoomsRoot.class);                 // Convert JSON File to Java Object
            retText.createMap();

            return retText;                                                 // return game map
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    // returns the items list object
    public ItemsList loadItems() {

        try (Reader reader = filegetter.getResource("items.json")) {  // try with resources
            return gson.fromJson(reader, ItemsList.class); // Convert JSON File to Java Object and return
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    // returns the hidden items list object
    public HiddenItemsList loadHiddenItems() {

        try (Reader reader = filegetter.getResource("hiddenitems.json")) {  // try with resources
            return gson.fromJson(reader, HiddenItemsList.class); // Convert JSON File to Java Object and return
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    // returns the iteractables list object
    public InteractablesList loadIteractables() {

        try (Reader reader = filegetter.getResource("interactables.json")) {  // try with resources
            return gson.fromJson(reader, InteractablesList.class); // Convert JSON File to Java Object and return
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }

    // returns the item use map object
    public ItemUseMap loadItemUseMap() {

        try (Reader reader = filegetter.getResource("itemuses.json")) {  // try with resources
            return gson.fromJson(reader, ItemUseMap.class);  // Convert JSON File to Java Object and return
        } catch (IOException err) {
            throw new RuntimeException(err);
        }
    }


    //-------------------------------ACCESSOR METHODS

    public List getRoomsList() {
        return roomsList;
    }

    public void setRoomsList(List roomsList) {
        this.roomsList = roomsList;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public List getHiddenItems() {
        return hiddenItems;
    }

    public void setHiddenItems(List hiddenItems) {
        this.hiddenItems = hiddenItems;
    }

    public List getInteractables() {
        return interactables;
    }

    public void setInteractables(List interactables) {
        this.interactables = interactables;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public Map<String, String> getItemUses() {
        return itemUses;
    }

    public void setItemUses(Map<String, String> itemUses) {
        this.itemUses = itemUses;
    }

    public Player getPlayer() {
        return player;
    }
}
