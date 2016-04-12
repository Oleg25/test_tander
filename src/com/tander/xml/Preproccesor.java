package com.tander.xml;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Created by velmOO on 12.04.2016.
 */
public class Preproccesor {

    /**
     * XSLT Transformation using style.xslt
     * @throws TransformerException
     */
    public static void transform() throws TransformerException {

        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("C:\\Users\\velmOO\\Downloads\\test\\src\\com\\tander\\xml\\style.xslt"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new File("C:\\Users\\velmOO\\Downloads\\test\\1.xml"));
        transformer.transform(text, new StreamResult(new File("2.xml")));

    }
}
