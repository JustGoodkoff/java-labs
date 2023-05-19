package ru.nsu.ccfit.gudkov.minesweeper.Model;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StatModel {

    public StatModel(){

    }

    public void getStatistic() {
        InputStream file = StatModel.class.getClassLoader().getResourceAsStream("./settings.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = null;
        try {
            doc = db.parse(file);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());




    }


}
