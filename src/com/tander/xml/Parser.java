package com.tander.xml;

/**
 * Created by velmOO on 12.04.2016.
 */

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Parser {

    /**
     *
     * @return int sum of field attribute values
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public static int countField()
            throws ParserConfigurationException,
            IOException, SAXException, XPathExpressionException {

        int sum =0;

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new File("C:\\Users\\velmOO\\Downloads\\test\\2.xml"));


        doc.getDocumentElement().normalize();

         XPath xPath = XPathFactory.newInstance().newXPath();
         XPathExpression expression = xPath.compile("//entries//entire");
         NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodeList.getLength(); ++i) {

            String s = ((Element)nodeList.item(i)).getAttribute("field");
            sum += Integer.parseInt(s);

        }

        return sum;


    }
}
