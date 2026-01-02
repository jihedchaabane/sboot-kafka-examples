package com.chj.gr.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//@RefreshScope
@ConfigurationProperties(prefix = "demo.json")
public class DemoParamsProperties {

	private String jobId;
	private Integer threadPoolNbre;
	private Integer chunkSize;
	private String jsonArrayPath;
	private String rowDataJossonFilePath;
	private String schemaValidationFilePath;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Integer getThreadPoolNbre() {
		return threadPoolNbre;
	}

	public void setThreadPoolNbre(Integer threadPoolNbre) {
		this.threadPoolNbre = threadPoolNbre;
	}

	public Integer getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(Integer chunkSize) {
		this.chunkSize = chunkSize;
	}

	public String getJsonArrayPath() {
		return jsonArrayPath;
	}

	public void setJsonArrayPath(String jsonArrayPath) {
		this.jsonArrayPath = jsonArrayPath;
	}

	public String getRowDataJossonFilePath() {
		return rowDataJossonFilePath;
	}

	public void setRowDataJossonFilePath(String rowDataJossonFilePath) {
		this.rowDataJossonFilePath = rowDataJossonFilePath;
	}
	
	public String getSchemaValidationFilePath() {
		return schemaValidationFilePath;
	}

	public void setSchemaValidationFilePath(String schemaValidationFilePath) {
		this.schemaValidationFilePath = schemaValidationFilePath;
	}

}