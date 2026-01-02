package com.chj.gr.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.StreamSupport;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class CodeUtils {

	private CodeUtils() {}
	
	public static String elapsedTime(Instant start) {
		Duration duration = Duration.between(start, Instant.now());
		return "Program execution took :"
				+ duration.toHoursPart() + " hours, "
				+ duration.toMinutesPart() + " minutes, "
				+ duration.toSecondsPart() + " seconds, "
				+ duration.toMillisPart() + " millis.";
	}
	
	
	@SuppressWarnings("unused")
	private static JsonArray filterJsonArray(JsonReader jsonReader, String jobId) {
		final JsonArray rows = JsonParser.parseReader(jsonReader).getAsJsonArray();
		StreamSupport.stream(rows.spliterator(), false)
		.map(JsonElement::getAsJsonObject)
		.filter(jsonObject ->
				jsonObject.get("source").getAsString().equals("PRo")
				&& jsonObject.get("type").getAsString().equals("RPT")
				&& jsonObject.get("name").getAsString().equals("TEST")
		)
		.forEach(jsonObject -> {
			jsonObject.add("active", new JsonPrimitive(true));
			jsonObject.add("jobId", new JsonPrimitive(jobId));
		});
		return rows;
	}
}
