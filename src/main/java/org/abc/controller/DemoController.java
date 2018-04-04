package org.abc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.abc.service.DemoRestService;

@RestController
public class DemoController {
	
	@Autowired
	private DemoRestService demoRestService;

	@GetMapping("/process")
	public String getProcess() {
	
	//return "abc";
		return demoRestService.processService();
	
}
}
