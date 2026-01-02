package com.chj.gr.service;

import java.util.List;
import java.util.concurrent.Callable;

import com.chj.gr.CallKafkaProducerService;
import com.chj.gr.RowData;
import com.chj.gr.rest.RequestDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyPersistenceCallable implements Callable<List<RowData>> {

	private CallKafkaProducerService callKafkaProducerService;
	private List<RowData> rowDatas;
	
	public MyPersistenceCallable(CallKafkaProducerService callKafkaProducerService, List<RowData> rowDatas) {
		this.rowDatas = rowDatas;
		this.callKafkaProducerService = callKafkaProducerService;
	}

	@Override
	public List<RowData> call() throws Exception {
		/**
		 * Call database persistence or whatever type of treatments, etc ...
		 */
//		Thread.sleep(2500);
		
		RequestDto requestDto = callKafkaProducerService.call(rowDatas);
		
		/**
		 * Do post call treatments here.
		 */
		log.info("Task {} is DONE.", Thread.currentThread().getName());
		log.info("RequestDta message back : {}.", requestDto.getMessage());
		return rowDatas;
	}

	
}
