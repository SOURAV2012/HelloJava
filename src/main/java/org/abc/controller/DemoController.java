package org.abc.controller;

import javax.xml.bind.JAXBException;

import org.abc.service.DemoRestService;
import org.abc.utils.DemoUtils; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.xml.sax.SAXException;

@RestController
public class DemoController {
	
	@Autowired
	private DemoRestService demoRestService;
	
	
	@GetMapping("/process")
	public String getProcess(@RequestParam("input")String inputXml){
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		String response = null;
		try {
			if(DemoUtils.validateXMLSchema(inputXml)){
				System.out.println("Calling Service");
				response = demoRestService.processService(inputXml,sessionId);
			}
			
			
			
		} catch (SAXException e) {
			response =  "Exception :: is"+e.getMessage();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			response =  "Exception :: Some Exception Occured" ;
		}
		
		return response ;
	}
	
	@GetMapping("/query")
	public String queryProcess(@RequestParam("node")Integer node){
		System.out.println("Inside Query");
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		String response = null;
		try {
			response = demoRestService.queryService(node, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
			response = "Exception :: "+e.getMessage();
		}
		return response;
	}
}
