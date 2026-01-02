package com.chj.gr.josson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octomix.josson.Josson;

@Service
public class JossonMappingService {

	@Autowired
	private ObjectMapper objectMapper;
	
	public Object mapAndTransform(String json, String mapping, Class<?> clazz) 
			throws JsonProcessingException {
		
		JsonNode transformedJson = Josson.fromJsonString(json).getNode(mapping);
		Object data = objectMapper.treeToValue(transformedJson, clazz);
		return data;
	}
}
