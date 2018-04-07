package org.abc.controller;

import javax.xml.bind.JAXBException;

import org.abc.service.DemoRestService;
import org.abc.utils.DemoUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.xml.sax.SAXException;

@RestController
@RequestMapping("/process")
public class DemoController {
	
	 static final Logger logger = Logger.getLogger(DemoController.class);

	
	@Autowired
	private DemoRestService demoRestService;
	
	
	@PostMapping(value ="/events") 
	public String getProcess(@RequestBody String inputXml){
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
			logger.error("The Error"+e);
			response = "Exception :: "+e.getMessage();
		}
		return response;
	}
}
