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

public class TMXLoader {
    private static final String ITEM_ELEMENT_NAME = "object";

    /**
     * Creates a Rectangle for each object in the room.
     * @param room Room with a TMX file to load from.
     * @return A map, the key being the item's name, and the value being the rectangle for that item.
     */
    public static Map<String, Rectangle> loadRoomItemRectangles(Room room) {
        // TMX (Tiled Map XML) file that will be parsed.
        String roomXML = room.getTMX();

        // Document with parsed XML.
        Document document = loadXML(roomXML);

        Map<String, Rectangle> rectangles = new HashMap<>();

        // Nodes in XML with the item information.
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

    /**
     * Loads XML file and parses it as a document.
     * @param filename The path to the xml file.
     * @return Document object with the parsed XML.
     */
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
