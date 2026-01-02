package com.chj.gr.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.chj.gr.CallKafkaProducerService;
import com.chj.gr.RowData;
import com.chj.gr.config.properties.DemoParamsProperties;
import com.chj.gr.josson.JossonMappingService;
import com.chj.gr.schemavalidation.SchemaValidationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.networknt.schema.JsonSchema;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TheService {

	private ExecutorService executorService;
	private List<Future<List<RowData>>> futures = new ArrayList<>();
	private AtomicInteger atomicInteger = new AtomicInteger(1);
	private final ReentrantLock reentrantLock = new ReentrantLock();
	private String jossonMapping;
	
	@Autowired
	private JossonMappingService jossonMappingService;
	@Autowired
	private DemoParamsProperties demoParamsProperties;
	
	@Autowired
	@Qualifier("jsonSchemaRowData")
	private JsonSchema jsonSchemaRowData;
	
	@Autowired
	private SchemaValidationService schemaValidationService;
	
	@Autowired
	private CallKafkaProducerService callKafkaProducerService;
	
	@PostConstruct
	private void init() {
		ThreadFactory namedThreadFactory =
				new ThreadFactoryBuilder().setNameFormat("my-jihed-thread-%d").build();
		executorService = Executors.newFixedThreadPool(demoParamsProperties.getThreadPoolNbre(), namedThreadFactory);
	
		try (InputStream inputStream = this.getClass().getResourceAsStream(
				demoParamsProperties.getRowDataJossonFilePath())) {
			jossonMapping = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public void validateSchema(String jsonObjectString) {
//		Set<ValidationMessage> errors = 
				schemaValidationService.validate(jsonObjectString, jsonSchemaRowData);
//		log.info(""+errors);
	}

	public JsonObject filterJsonObjectIfNeeded(JsonObject jsonObject, String jobId) {
//		if (jsonObject.get("source").getAsString().equals("PRo")
//				&& jsonObject.get("type").getAsString().equals("RPT")
//				&& jsonObject.get("name").getAsString().equals("TEST")) {
			/**
			 * Add ab brand new properties.
			 */
			jsonObject.add("jobId", new JsonPrimitive(jobId));
			jsonObject.add("chunkId", new JsonPrimitive(atomicInteger.intValue()));
			/**
			 * Update an already existing property.
			 */
			jsonObject.add("active", new JsonPrimitive(true));
			return jsonObject;
//		}
//		return null;
	}
	
	public void process(AtomicReference<List<RowData>> rowDatas, JsonObject jsonObject) {
		if (jsonObject != null && jsonObject.has("jobId") && jsonObject.has("chunkId")) {
//			RowData mapped = (RowData) JsonUtils.toPojo(jsonObject.getAsJsonObject(), RowData.class);
			/**
			 * START JOSSON.
			 */
			try {
				RowData mapped = 
						(RowData) jossonMappingService.mapAndTransform(
										jsonObject.toString(), jossonMapping, RowData.class);
				/**
				 * END JOSSON.
				 */
				if (mapped.getIdentities() != null) {
					mapped.getIdentities().stream().forEach(t -> {
						RowData row = SerializationUtils.clone(mapped);
						row.setIdentities(Arrays.asList(t));
						rowDatas.get().add(row);
						if (rowDatas.get().size() == demoParamsProperties.getChunkSize()) {
							this.cloneClearDispatch(rowDatas);
						}
					});
				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	public void cloneClearDispatch(AtomicReference<List<RowData>> rowDatas) {
		AtomicReference<List<RowData>> o = SerializationUtils.clone(rowDatas);
		rowDatas.get().clear();
		this.dispatch(o);
	}

	private void dispatch(AtomicReference<List<RowData>> o) {
		this.futures.add(
					executorService.submit(new MyPersistenceCallable(callKafkaProducerService, o.get())));
		this.increment();
	}

	private synchronized void increment() {
		reentrantLock.lock();  // Acquire lock
		atomicInteger.getAndIncrement();
		reentrantLock.unlock();  // Release lock
	}
	
	private long howMuchProcessed;
	public void waitForAllDone() {
		/**
		 * Wait for the result of all Future(s).
		 */
		this.futures.stream().forEach(f -> {
			try {
				List<RowData> rowDatas = f.get();
				howMuchProcessed += rowDatas.size();
//				rowDatas.forEach(System.out::println);
				log.info("Computation result size {} of chunk {}.", rowDatas.size(), rowDatas.get(0).getChunkId());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.executorService.shutdown();
			}
		});
		log.info("{} chunks processed with total of {} rows!!", atomicInteger.get()-1, howMuchProcessed);
	}
}
