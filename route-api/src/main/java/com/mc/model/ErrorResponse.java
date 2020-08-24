package com.mc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mc.exception.ExceptionType;

public class ErrorResponse {
	private static final String LOCAL_DATE_TIME_FORMAT = "MM/dd/YYYY HH:mm:ss";
	private String timestamp;
	private String message;
	private ExceptionType error;

	public ErrorResponse() {
		final LocalDateTime localDateTime = LocalDateTime.now();
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);
		timestamp = localDateTime.format(formatter);
	}

	public ErrorResponse(ExceptionType error, String message) {
		this();
		this.error = error;
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ExceptionType getError() {
		return error;
	}

	public void setError(ExceptionType error) {
		this.error = error;
	}

}