package com.lostinspace.app;

import com.lostinspace.controller.GUIController;
import com.lostinspace.model.Model;

public class GUIApp {
    public static void main(String[] args) {
        Model model = new Model();
        GUIController controller = new GUIController(model);
        controller.execute();
    }
}
