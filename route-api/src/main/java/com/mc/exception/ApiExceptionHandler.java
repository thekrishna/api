package com.mc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.mc.model.ErrorResponse;

/**
 * Global Exception Handler to handle all exceptions and responsible for
 * including the custom error message in HTTP Response
 * 
 * @author Krishna Kumar
 */
@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorResponse> handleAllExceptions(final Exception ex, final WebRequest request) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(ex.getMessage());
		errorResponse.setError(ExceptionType.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ApiException.class)
	public final ResponseEntity<ErrorResponse> handleApiException(final ApiException ex, final WebRequest request) {
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setError(ex.getExceptionType());
		errorResponse.setMessage(ex.getMessage());
		return new ResponseEntity<>(errorResponse, errorResponse.getError().getHttpStatus());
	}

}