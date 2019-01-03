package com.kk.api.station.exception;

/**
 * This class used to define the error code for processing Station API errors.
 *  @author Krishna Kumar
 */
public enum StationApiError {
	
	NOT_FOUND("404","Station not found"),
	UNKNOWN_ERROR("500","Unknown Error"),
	ALREADY_EXISTS("409","Station already exist");
	
	private String errorCode;
	private String errorMessage;
	
	/**
	 * Creates instance of StationApiError
	 * @param errorCode
	 * @param errorMessage
	 */
	StationApiError(final String errorCode,final String errorMessage){
		this.errorCode=errorCode;
		this.errorMessage=errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
