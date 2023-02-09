package com.lostinspace.util;

public class  TextPrinter {
    /*
     * Send text to console
     */
    public static void displayText(String text) {
        System.out.println(text);
    }

    public static void displayText(String text, Color color) {
        System.out.println(color.setFontColor(text));
    }

    /*
     * Send text to GUI
     */
    private static void sendToGUI(String text) {

    }

    private static void sendToGUI(String text, Color color) {

    }
}
