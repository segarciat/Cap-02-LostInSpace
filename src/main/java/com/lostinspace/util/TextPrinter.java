package com.lostinspace.util;

class TextPrinter {
    public static void displayText(String text) {
        System.out.println(text);
    }

    public static void displayText(String text, Color color) {
        System.out.println(color.setFontColor(text));
    }
}
