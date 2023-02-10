package com.lostinspace.view;

import javax.swing.*;
import javax.swing.text.Document;

class MainTextArea extends JTextArea {
    private String text;

    public MainTextArea() {
    }

    public MainTextArea(String text) {
        super(text);
    }

    public MainTextArea(int rows, int columns) {
        super(rows, columns);
    }

    public MainTextArea(String text, int rows, int columns) {
        super(text, rows, columns);
    }

    public MainTextArea(Document doc) {
        super(doc);
    }

    public MainTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
    }
}
