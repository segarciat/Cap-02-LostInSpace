package com.lostinspace.util;/*

 * Game com.game.utilities.Controller | Author: Mike Greene
 * The player controller script for text adventure Lost in Space.
 * Handles all player commands and their feedback.
 */

//import crayons;
//import json;
//import time;

import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;

// handles user commands and their feedback
// stores inventory data
// loads game text/dialogue
class Controller {
    Gson gson = new Gson(); // create new Gson object for converting JSON objs

    // variables for string coloring
    public static final String ANSI_RESET = "\u001B[0m"; // resets the color

    // color values
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    // displays general instructions to player as a reminder
    public void showInstructions() {
        String instructions = new StringBuilder()
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
                .append("*objectives - \n\n")
                .append("*new[game] - restart the game\n\n")
                .append("*quit[game] - quits the current game.\n\n")
                .toString();

        System.out.println(instructions);
    }

    //determine the current status of the player
    // displays current location, inventory, and Oxygen levels
    public void showStatus() {
         //clear text from terminal

        //print the player 's current location
        System.out.println(ANSI_YELLOW + "---------------------------" + ANSI_RESET);
        //System.out.println("You are in the " + currentRoom + '\n');

        // print description of current room
        //todo Add Room Description to showStatus()
        System.out.println(ANSI_GREEN + "DESCRIPTION HERE" + ANSI_RESET + "\n\n");

        // print what the player is carrying
        //String result = String.join(",", inventory);
        //System.out.println(ANSI_BLUE + String.format("\n\nInventory: %s", result) + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "---------------------------" + ANSI_RESET);
    }

    public final static void clearConsole() {
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

    // an inventory, which is initially empty
    List<String> inventory = Arrays.asList();

    // loads an object which acts as a map
    String str = gson.fromJson("\"abc\"", String.class);
//    with open("dungeon_map.json","r")as map_file:
//    rooms =json.load(map_file)
//
//
//            #
//    start the
//    player in
//    the Hall
//    currentRoom ='Hall'
//
//    showInstructions()
//
//        #breaking this while
//    loop means
//    the game
//    is over
//        while True:
//
//    showStatus()
//
//        #
//    the player
//    MUST type
//    something in
//        #
//    otherwise input
//    will keep
//    asking
//            move = ''
//        while move =='':
//    move =
//
//    input('>')
//
//        #
//    normalizing input:
//            # .
//
//    lower() makes it lower
//        case,.
//
//    split() turns it to a list
//        #therefore,"get golden key"becomes["get","golden key"]
//    move =move.lower().
//
//    split(" ",1)
//
//        #if
//    they type 'go' first
//        if move[0]=='go':
//            #
//    check that
//    they are
//    allowed wherever
//    they want
//    to go
//        if move[1]
//    in rooms[
//    currentRoom]:
//            #
//    set the
//    current room
//    to the new
//    room
//            currentRoom = rooms[currentRoom][move[1]]
//        #if
//    they aren 't allowed to go that way:
//            else:
//
//    print(crayons.red('You can\'t go that way!', bold =True))
//            time.sleep(1)
//
//            #if
//    they type 'get' first
//        if move[0]=='get':
//            #
//    make two
//    checks:
//            #1.if
//    the current
//    room contains
//    an item
//        #2.if
//    the item
//    in the
//    room matches
//    the item
//    the player
//    wishes to
//    get
//        if"item"
//    in rooms[
//    currentRoom]
//    and move[ 1]
//    in rooms[
//    currentRoom]['item']:
//            #
//    add the
//    item to
//    their inventory
//        inventory.append(move[1])
//            #
//    display a
//    helpful message
//
//    print(f '\nYou got the {crayons.magenta(move[1])}!\n\n')
//
//    input(crayons.blue("Press ENTER to continue"))
//            #
//    delete the
//    item key:
//    value pair
//    from the
//    room 's dictionary
//    del rooms[
//    currentRoom]['item']
//            #if there 's no item in the room or the item doesn'
//    t match
//        else:
//                #
//    tell them
//    they can 't get it
//
//    print('Can\'t get '+move[1]+'!')
//
//        ##
//    If a
//    player enters
//    a room
//    with a
//    monster
//        if'item'
//    in rooms[
//    currentRoom]and 'monster'
//    in rooms[
//    currentRoom]['item']:
//
//    print('A monster has got you... GAME OVER!')
//        break
//
//                ##
//    Define how
//    a player
//    can win
//        if currentRoom =='Garden' and 'key'
//    in inventory
//    and 'potion'
//    in inventory:
//
//    print('You escaped the house with the ultra rare key and magic potion... YOU WIN!')
//        break

}
