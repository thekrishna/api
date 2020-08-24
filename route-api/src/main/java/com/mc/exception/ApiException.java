package com.mc.exception;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private ExceptionType exceptionType;

	public ApiException(final String errorMessage, final ExceptionType exceptionType) {
		super();
		this.exceptionType = exceptionType;
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}
}
