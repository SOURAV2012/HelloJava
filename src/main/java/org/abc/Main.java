
package org.abc;

import org.abc.utils.DemoUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class Main {

	 static final Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws SAXException {

		String s="<?xml version=\"1.0\" encoding=\"UTF-8\"?><root> <element> <parent>0</parent> <nodename>1</nodename> <value>This is the first node</value> </element></root>";
		  
		logger.info(DemoUtils.validateXMLSchema(s));
	}

}
