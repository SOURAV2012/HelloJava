package org.abc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.abc.controller.DemoController;
import org.abc.service.DemoRestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRestApplicationTests {
	
	@Autowired
	private DemoController controller;
	
	@Autowired
	private DemoRestService service;

	@Test
	public void contextLoads() {
        assertThat(controller).isNotNull();

	}
	
	@Test
	public void testInputXML() {
        
		String inputXml=null;
		
		assertNotEquals(true, controller.getProcess(inputXml));

	}
	
	
	

}
