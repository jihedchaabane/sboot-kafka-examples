package com.chj.gr;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.chj.gr.config.properties.DemoParamsProperties;
import com.chj.gr.service.TheService;
import com.chj.gr.utils.CodeUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@SpringBootApplication
public class RowDataFileConsumerApplication implements CommandLineRunner {
	@Autowired
	private ConfigurableApplicationContext context;
	
	@Autowired
	private TheService theService;
	
	@Autowired
	private DemoParamsProperties demoParamsProperties;
	
	
//	@Autowired
//	private CallKafkaProducerService callKafkaProducerService;

	public static void main(String[] args) {
		SpringApplication.run(RowDataFileConsumerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		callKafkaProducerService.call();
		
		this.begin();
		
		context.close();
	}

	public void begin() {
		Instant start = Instant.now();
		AtomicReference<List<RowData>> rowDatas = new AtomicReference<>(new ArrayList<RowData>());
		try (JsonReader jsonReader = new JsonReader(
				new InputStreamReader(
						new FileInputStream(
								System.getProperty("user.dir").concat(demoParamsProperties.getJsonArrayPath()))
								, StandardCharsets.UTF_8))) {
			jsonReader.beginArray();
			while (jsonReader.hasNext()) {
				JsonObject jsonObject = JsonParser.parseReader(jsonReader).getAsJsonObject();
				theService.validateSchema(jsonObject.toString());
				
				jsonObject = theService.filterJsonObjectIfNeeded(jsonObject, demoParamsProperties.getJobId());
				theService.process(rowDatas, jsonObject);
			}
			jsonReader.endArray();
			
			if (rowDatas.get().size() > 0) {
				theService.cloneClearDispatch(rowDatas);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			theService.waitForAllDone();
		}
		
		System.out.println(CodeUtils.elapsedTime(start));
	}


}