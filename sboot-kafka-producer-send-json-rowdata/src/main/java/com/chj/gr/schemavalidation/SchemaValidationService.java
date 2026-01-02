package com.chj.gr.schemavalidation;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

@Service
public class SchemaValidationService {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public Set<ValidationMessage> validate(String body, JsonSchema jsonSchema) {

		Set<ValidationMessage> errors = null;
		try {
			JsonNode node = objectMapper.readTree(body);
			errors = jsonSchema.validate(node);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (errors != null && !errors.isEmpty()) {
			errors.stream().map(ValidationMessage::getMessage).toList().forEach(System.err::println);
		}
		return errors;
	}

}
