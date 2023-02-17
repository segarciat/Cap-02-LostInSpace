//package com.lostinspace.view;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionListener;
//
//import static org.junit.Assert.*;
//
//public class OpenDialogActionTest {
//    JFrame frame = new JFrame();
//    String text = "Text test in swing";
//    String title = "Lost In Space";
//    OpenDialogAction action = new OpenDialogAction(frame,text, title);
//
//    @Test
//    public void shouldTestFieldSettings() {
//        //Test settings of fields
//        assertEquals(frame, action.frame);
//        assertEquals(text, action.textArea.getText());
//        assertEquals(title, action.title);
//    }
//
//    @Test
//    public void shouldPerformDialogueActionWhenCalled() {
//        // Action performed method
//        action.actionPerformed(null);
//        assertNotNull(action.d);
//        assertEquals(OpenDialogAction.WINDOW_SIZE, action.d.getSize().width);
//        assertEquals(OpenDialogAction.WINDOW_SIZE, action.d.getSize().height);
//        assertEquals(frame,action.d.getOwner());
//}
//
//    @Test
//    public void buttonClosesWhenTested(){
//        // Test Closed button
//        ActionListener[] listeners = action.closeButton.getActionListeners();
//        assertEquals(1,listeners.length);
//        listeners[0].actionPerformed(null);
//        assertTrue(action.d.isVisible());
//    }
//
//}

