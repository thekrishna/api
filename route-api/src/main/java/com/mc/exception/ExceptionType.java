package com.mc.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionType {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An internal server error occured"),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "BadRequest"), NOT_FOUND(HttpStatus.NOT_FOUND, "NotFound");

	private HttpStatus httpStatus;
	private String message;

	ExceptionType(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}

}
