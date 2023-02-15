package com.lostinspace.util;

/**
 * Displays text in the console.
 */
public class TextPrinter {
    /**
     * Prints in the console.
     * @param text The text to be displayed.
     */
    public static void displayText(String text) {
        System.out.println(text);
    }

    /**
     * Prints text in the console in a foreground color of the client's choice.
     * @param text The text to be displayed.
     * @param color The foreground color for the text.
     */
    public static void displayText(String text, Color color) {
        System.out.println(color.setFontColor(text));
    }
}
