package org.abc.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.abc.mapper.ElementType;
import org.abc.mapper.ObjectFactory;
import org.abc.mapper.RootType;
import org.abc.utils.ApplicationConstant;
import org.springframework.stereotype.Service;



@Service
public class DemoRestService {
	
	private static Map<String,List<ElementType>> processResponseCache = new HashMap<String,List<ElementType>>();
	
	 Comparator<ElementType> elementComparator = Comparator.comparing(ElementType::getParent , (e1,e2) ->{
														if(e1==e2)
															return 0 ;
														else if(e1>e2)
															return 1;
														else
															return 0;
												});
	
	public String processService(String input , String sessionId) throws JAXBException{
		 List<ElementType> elementList = processXmlElements(input);
		 processResponseCache.put(sessionId,elementList);
		 return printIndented(elementList,0);
	}
	
	
	public String queryService(Integer node , String sessionId) throws Exception{
		
		if(!sessionId.contains(sessionId)){
			throw new Exception("Process Request not Found in the Session");
		}	
		
		List<ElementType> elementList  = processResponseCache.get(sessionId);		
		elementList =	elementList.stream()
								    .filter(n -> (n.getParent() >= node && n.getParent() <= node+2))
								    .sorted(elementComparator)
								    .collect(Collectors.toList());
		
		return printIndented(elementList,node);
		 
	}
	
	
	private  List<ElementType> processXmlElements(String xmlElements) throws JAXBException{
		 
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		InputStream stream = new ByteArrayInputStream(xmlElements.getBytes(StandardCharsets.UTF_8));		 
		 RootType root = ((JAXBElement<RootType>)  jaxbUnmarshaller.unmarshal(stream)).getValue();;	

		 
		 
		 List<ElementType> elementList =    root.getElement()
										 	 .stream()
										 	 .sorted(elementComparator)
										 	 .collect(Collectors.toList());
		 
		 
		 return elementList;
	}
	
	
	private String printIndented(List<ElementType> elementList, int startNode){
		
		String indent = "";
		StringBuilder responseBuilder = new StringBuilder()
											.append(ApplicationConstant.HTML_TAG_OP)
											.append(ApplicationConstant.HTML_TAG_P_OP)
											.append(elementList.get(0).getParent())
											.append(ApplicationConstant.HTML_TAG_P_CL);
		int prevNode = -1;
		for(ElementType element : elementList){
			if(prevNode != element.getParent()){
				indent = indent + ApplicationConstant.SPACE;
				responseBuilder.append(ApplicationConstant.HTML_TAG_P_OP)
								.append(indent)
								.append(element.getNodename())
								.append(ApplicationConstant.HTML_TAG_P_CL);
			}else{
				responseBuilder.append(ApplicationConstant.HTML_TAG_P_OP)
								.append(indent)
								.append(element.getNodename())
								.append(ApplicationConstant.HTML_TAG_P_CL);
			}
			prevNode = element.getParent();
		}
		responseBuilder.append(ApplicationConstant.HTML_TAG_CL);
		return responseBuilder.toString();		
	}

}
