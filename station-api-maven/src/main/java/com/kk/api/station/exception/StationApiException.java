package com.kk.api.station.exception;

import java.util.EnumMap;
import java.util.Objects;

import org.springframework.http.HttpStatus;

/**
 * 
 * Station API exception use to throw the Station API error while processing Station API request.
 * 
 * @author Krishna Kumar
 *
 */
public class StationApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private static final String UNKNOWN_ERROR = "Unknown Error";
	private StationApiError stationApiError;
	private String rootMessage;
	private HttpStatus httpStatus;
	
	public enum StationApiResponseCode{
		CONFLICT,BAD_REQUEST,INTERNAL_SERVER_ERROR,NOT_FOUND,INVALID_REQUEST
	}
	private static EnumMap<StationApiResponseCode,HttpStatus> httpStatusMap;
	static {
		httpStatusMap = new EnumMap<StationApiResponseCode,HttpStatus>(StationApiResponseCode.class);
		httpStatusMap.put(StationApiResponseCode.CONFLICT, HttpStatus.CONFLICT);
		httpStatusMap.put(StationApiResponseCode.BAD_REQUEST, HttpStatus.BAD_REQUEST);
		httpStatusMap.put(StationApiResponseCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		httpStatusMap.put(StationApiResponseCode.NOT_FOUND, HttpStatus.NOT_FOUND);
		httpStatusMap.put(StationApiResponseCode.INVALID_REQUEST, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Creates instance of {@link StationApiException} by passing
	 * {@link StationApiError}
	 * 
	 * @param stationApiError{@link StationApiError} to be thrown
	 * @param stationApiResponseCode{@link StationApiResponseCode} HttpStatus to be returned in response.
	 * @see #buildMessage(StationApiError)
	 */
	public StationApiException(final StationApiError stationApiError,final StationApiResponseCode stationApiResponseCode) {
		super(buildMessage(stationApiError));
		this.stationApiError = stationApiError;
		this.rootMessage = buildMessage(stationApiError);
		this.httpStatus = httpStatusMap.get(stationApiResponseCode);
	}

	/**
	 * Creates instance of {@link StationApiException} by passing
	 * {@link StationApiError} and {@link Throwable} for root cause
	 * 
	 * @param stationApiError
	 *            {@link StationApiError} to be thrown
	 * @param cause
	 *            {@link Throwable} throwable as a part of the exception
	 */
	public StationApiException(final StationApiError stationApiError, final Throwable cause) {
		super(cause);
		this.stationApiError = stationApiError;
		this.rootMessage = cause.getLocalizedMessage();
	}

	/**
	 * Builds error message using {@link StationApiError}
	 * {@code StationApiError.getErrorMessage()}
	 * 
	 * @param stationApiError
	 *            {@link StationApiError}
	 * @return error message using {@link StationApiError.getErrorMessage()}
	 */
	private static String buildMessage(StationApiError stationApiError) {
		return Objects.isNull(stationApiError) ? UNKNOWN_ERROR : stationApiError.getErrorMessage();
	}

	/**
	 * Return error message using the error message using the
	 * {@link StationApiError} and user message for root cause
	 */
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		if (Objects.nonNull(stationApiError)) {
			builder.append("StationApiError [ErrorCode=");
			builder.append(stationApiError.getErrorCode());
			builder.append(", ErrorMessage=");
			builder.append(stationApiError.getErrorMessage());
			builder.append("]");
		}
		return builder.toString();
	}

	/**
	 * @return the root cause
	 */
	public String getRootMessage() {
		return rootMessage;
	}

	/**
	 * @return the stationApiError
	 */
	public StationApiError getStationApiError() {
		return stationApiError;
	}

	/**
	 * @return the httpStatus
	 */
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
	

}
