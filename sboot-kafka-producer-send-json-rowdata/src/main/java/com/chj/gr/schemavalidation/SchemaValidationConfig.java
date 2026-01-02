package com.chj.gr.schemavalidation;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chj.gr.config.properties.DemoParamsProperties;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

@Configuration
public class SchemaValidationConfig {
	
	@Autowired
	private DemoParamsProperties demoParamsProperties;

	@Bean("jsonSchemaRowData")
	public JsonSchema jsonSchemaRowData() {
		JsonSchema jsonSchema = null;
		try (InputStream inputStream = getClass().getResourceAsStream(
				demoParamsProperties.getSchemaValidationFilePath())) {
			JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);
			jsonSchema = factory.getSchema(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load schema file " 
						+ demoParamsProperties.getSchemaValidationFilePath(), e);
		}
		return jsonSchema;
	}

}
