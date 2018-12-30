package com.alas.translator;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XmlWriter {
	
	String from;
	String to;
	String filePath = "E:\\xmlfile.xml";
	Date date= new Date();
	long time = date.getTime();
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	
	public XmlWriter(String from, String to) {
		this.from = from;
		this.to = to;
		Timestamp timestamp = new Timestamp(time);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            if(new File(filePath).exists()) {
            	File xmlFile = new File(filePath);
            	
            	Document doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();
                
                NodeList rootElements = doc.getElementsByTagName("translations");
                Element rootElement = doc.getDocumentElement();
                
                NodeList translations = doc.getElementsByTagName("translation");
                int i = translations.getLength();
                
                rootElement.appendChild(getTranslation(doc, timestamp, String.valueOf(i+1), from, to));
                
                doc.getDocumentElement().normalize();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
                System.out.println("XML file updated successfully");
                
                
            }else {
	            Document doc = dBuilder.newDocument();
	            //add elements to Document
	            Element rootElement =
	                doc.createElement("translations");
	            //append root element to document
	            doc.appendChild(rootElement);
	            
	            rootElement.appendChild(getTranslation(doc, timestamp, "1", from, to));
	
	             //for output to file, console
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            //for pretty print
	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            DOMSource source = new DOMSource(doc);
	
	            //write to console or file
	            StreamResult console = new StreamResult(System.out);
	            StreamResult file = new StreamResult(new File(filePath));
	
	            //write data
	            transformer.transform(source, console);
	            transformer.transform(source, file);
	            System.out.println("DONE");
            }
	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	}

    private static Node getTranslation(Document doc,Timestamp timestamp, String id, String from, String to) {
        Element translation = doc.createElement("translation");

        //set timestamp and id attributes
        translation.setAttribute("timestamp", timestamp.toString());
        translation.setAttribute("id", id);

        //create from element
        translation.appendChild(getTranslationElements(doc, translation, "from", from));

        //create to element
        translation.appendChild(getTranslationElements(doc, translation, "to", to));

        return translation;
    }


    //utility method to create text node
    private static Node getTranslationElements(Document doc, Element element, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
	

       
}
