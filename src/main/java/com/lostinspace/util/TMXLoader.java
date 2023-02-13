package com.lostinspace.util;

import com.lostinspace.model.Room;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class TMXLoader {
    private static final String XML_FILE = "xml/crew_quarters.tmx";

    private static final String ITEM_ELEMENT_NAME = "object";

    public static void main(String[] args) {
        Document document = loadXML(XML_FILE);
        Map<String, Rectangle> allItems = getAllItems(document);
        System.out.println(allItems);
    }

    public static Map<String, Rectangle> getRoomItemRectangles(Room room) {
        Map<String, Rectangle> rectangles = new HashMap<>();

        String roomXML = "";
        // String roomXML = room.getTMX();

        Document document = loadXML(roomXML);

        return getAllItems(document);
    }

    public static Map<String, Rectangle> getAllItems(Document document) {
        Map<String, Rectangle> rectangles = new HashMap<>();

        NodeList itemNodes = document.getElementsByTagName(ITEM_ELEMENT_NAME);
        for(int i=0; i<itemNodes.getLength(); i++)
        {
            Node itemNode = itemNodes.item(i);
            if(itemNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element itemElement = (Element) itemNode;
                String itemName = itemElement.getAttribute("name");
                int x = Integer.parseInt(itemElement.getAttribute("x"));
                int y = Integer.parseInt(itemElement.getAttribute("y"));
                int width = Integer.parseInt(itemElement.getAttribute("width"));
                int height = Integer.parseInt(itemElement.getAttribute("height"));

                Rectangle itemRectangle = new Rectangle(x, y, width, height);

                rectangles.put(itemName, itemRectangle);
            }
        }
        return rectangles;
    }

    public static Document loadXML(String filename) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            return builder.parse(TMXLoader.class.getClassLoader().getResourceAsStream(filename));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
