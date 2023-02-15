package com.lostinspace.util;

import com.lostinspace.controller.Controller;

import java.util.Scanner;

public class GameEvents {
    Scanner scanner = new Scanner(System.in);

    /**
     * Waits for user to press enter, and clears the screen.
     */
    public void enterToContinue() {
        System.out.println("\n\n--- Press ENTER ---"); // Tell user how to continue
        scanner.nextLine(); // consume line of input
        Controller.clearConsole();     // clear the console
    }
}

