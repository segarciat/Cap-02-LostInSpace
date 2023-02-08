package com.lostinspace.util;

import com.lostinspace.controller.Controller;

import java.util.Scanner;

public class GameEvents {
    Scanner scanner = new Scanner(System.in);

    public void enterToContinue() {
        System.out.println("\n\n--- Press ENTER ---"); // Tell user how to continue
        scanner.nextLine(); // consume line of input
        Controller.clearConsole();     // clear the console
    }
}

