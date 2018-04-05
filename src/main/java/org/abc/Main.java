package org.abc;

import org.abc.utils.DemoUtils;
import org.xml.sax.SAXException;

public class Main {

	public static void main(String[] args) throws SAXException {
		// TODO Auto-generated method stub

		String s="<?xml version=\"1.0\" encoding=\"UTF-8\"?><root> <element> <parent>0</parent> <nodename>1</nodename> <value>This is the first node</value> </element></root>";
		  
		System.out.println(DemoUtils.validateXMLSchema(s));
	}

}
