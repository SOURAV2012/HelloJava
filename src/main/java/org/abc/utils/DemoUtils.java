package org.abc.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class DemoUtils {
	
	private static Schema _schema = null;
	
public static boolean validateXMLSchema(String xmlString) throws SAXException{
        
        try {
           
            Validator validator = getSchema().newValidator();
            InputStream stream=new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
            validator.validate(new StreamSource(stream));
        } catch (IOException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }

private static Schema getSchema() {
	
	 SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
     try {
    	 if(_schema==null)
    		 _schema = factory.newSchema(DemoUtils.class.getResource("/xsd/hierarchy.xsd"));
    	 
	} catch (SAXException e) {
		e.printStackTrace();
		
	}
	 return _schema;

	
}

}
