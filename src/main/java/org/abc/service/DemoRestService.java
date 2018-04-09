package org.abc.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.abc.mapper.ElementType;
import org.abc.mapper.ObjectFactory;
import org.abc.mapper.RootType;
import org.abc.utils.ApplicationConstant;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DemoRestService {

	final static Logger logger = Logger.getLogger(DemoRestService.class);

	private static Map<String, Map<Integer, List<ElementType>>> processResponseCache = new HashMap<String, Map<Integer, List<ElementType>>>();

	Comparator<ElementType> elementComparatorParent = Comparator.comparing(ElementType::getParent, (e1, e2) -> {
		if (e1 == e2)
			return 0;
		else if (e1 > e2)
			return 1;
		else
			return -1;
	});

	Comparator<ElementType> elementComparatorNodename = Comparator.comparing(ElementType::getNodename, (e1, e2) -> {
		if (e1 == e2)
			return 0;
		else if (e1 > e2)
			return 1;
		else
			return -1;
	});

	public String processService(String input, String sessionId) throws JAXBException {
		logger.debug("Inside processService()");
		Map<Integer, List<ElementType>> elementMap = processXmlElements(input);
		processResponseCache.put(sessionId, elementMap);
		return printIndented(elementMap, 0 ,-1);
	}

	public String queryService(Integer startNode, String sessionId) throws Exception {
		logger.debug("Inside queryService()");
		if (!sessionId.contains(sessionId)) {
			throw new Exception("Process Request not Found in the Session");
		}
		Map<Integer, List<ElementType>> elementMap = processResponseCache.get(sessionId);
		return printIndented(elementMap, startNode,1);
	}

	private Map<Integer, List<ElementType>> processXmlElements(String xmlElements) throws JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		InputStream stream = new ByteArrayInputStream(xmlElements.getBytes(StandardCharsets.UTF_8));
		RootType root = ((JAXBElement<RootType>) jaxbUnmarshaller.unmarshal(stream)).getValue();

		Map<Integer, List<ElementType>> elementMap = new TreeMap<Integer, List<ElementType>>();

		root.getElement().stream().forEach(n -> {
			if (elementMap.containsKey(n.getParent())) {
				List eList = elementMap.get(n.getParent());
				eList.add(n);
				Collections.sort(eList, elementComparatorNodename);
			} else {
				List eList = new ArrayList<ElementType>();
				eList.add(n);
				elementMap.put(n.getParent(), eList);
			}
		});

		return elementMap;
	}

	private static void print(Map<Integer, List<ElementType>> elementMap, int startNode, StringBuilder responseBuilder,
			String indent, Integer initLevel, Integer finalLevel) {

		if (initLevel > finalLevel) {
			return;
		}

		if (!elementMap.containsKey(startNode)) {
			return;
		}
		List<ElementType> lst = elementMap.get(startNode);
		indent = indent + ApplicationConstant.SPACE;

		for (ElementType elmt : lst) {

			if (elmt.getNodename() == startNode) {
				continue;
			}
			responseBuilder.append(System.getProperty("line.separator")).append(indent).append(elmt.getNodename());

			if (initLevel == -1) {
				print(elementMap, elmt.getNodename(), responseBuilder, indent, initLevel, finalLevel);
			} else {
				print(elementMap, elmt.getNodename(), responseBuilder, indent, initLevel + 1, finalLevel);
			}

		}

	}

	private String printIndented(Map<Integer, List<ElementType>> elementMap, int startNode,Integer initLevel) {

		
		Integer finalLevel = initLevel + 1; // for two level penetration

		String indent = "";
		StringBuilder responseBuilder = new StringBuilder();

		responseBuilder.append(startNode);

		print(elementMap, startNode, responseBuilder, indent, initLevel, finalLevel);
		return responseBuilder.toString();
	}

}
