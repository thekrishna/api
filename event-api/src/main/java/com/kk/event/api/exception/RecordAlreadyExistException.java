package com.kk.event.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RecordAlreadyExistException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public RecordAlreadyExistException(final String error) {
		super(error);
	}

}
