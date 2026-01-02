package com.chj.gr;

import java.util.Arrays;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chj.gr.rest.RequestDto;

@Service
public class CallKafkaProducerService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${server.port}")
	private String serverPort;
	
	public RequestDto call(List<RowData> rows) {
		
		URIBuilder uriBuilder = new URIBuilder()
			    .setScheme("http")
			    .setHost("localhost")
			    .setPort(1806)
			    .setPath("/ms6/topics/bytype/send/requestDtos");
		
		RequestDto requestDto = RequestDto.builder()
				.data(rows)
				.message("hello")
			.build();
		/**
		 * WORKS fine.
		 */
//		requestDto = restTemplate.postForEntity(
//				uriBuilder.toString()
//				, requestDto
//				, RequestDto.class).getBody();
		
		/**
		 * WORKS fine.
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("spring.application.name", applicationName);
		headers.set("server.port", serverPort);
		HttpEntity<RequestDto> entity = new HttpEntity<>(requestDto, headers);
		requestDto = restTemplate.postForEntity(
				uriBuilder.toString()
				, entity
				, RequestDto.class).getBody();
		/**
		 * WORKS fine.
		 */
//		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
//		parts.add("requestDto", requestDto);
//		requestDto = restTemplate.postForEntity(
//				uriBuilder.toString()
//				, parts
//				, RequestDto.class).getBody();
		
		return requestDto;
	}
}
