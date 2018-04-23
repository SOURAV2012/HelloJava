package org.abc.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.abc.service.DemoRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
@RequestMapping("/process")
public class DemoController {

	final static Logger logger = Logger.getLogger(DemoController.class);

	@Autowired
	private DemoRestService demoRestService;

	@PostMapping(value = "/events")
	public String getProcess(@RequestBody String inputXml) {
		if(inputXml == null ||  inputXml.trim().isEmpty())
		{
			return "Error :: Empty Request Body";
		}
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		String response = null;
		try {
				response = demoRestService.processService(inputXml, sessionId);
		} catch (JAXBException e) {
			response = "Exception :: Some Exception Occured";
		}

		return response;
	}
		

	@GetMapping("/query")
	public String queryProcess(@RequestParam("node") Integer node) {
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
		String response = null;
		try {
			response = demoRestService.queryService(node, sessionId);
		} catch (Exception e) {
			logger.error("The Error" + e);
			response = "Exception :: " + e.getMessage();
		}
		return response;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	void handleBadRequests(HttpServletResponse response) throws IOException {
	    response.sendError(HttpStatus.BAD_REQUEST.value(), "Please try again and with a non empty string as 'name'");
	}
	
	
}