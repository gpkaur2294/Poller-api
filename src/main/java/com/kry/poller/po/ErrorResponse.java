package com.kry.poller.po;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ErrorResponse {
	private int code;
	private String message;
	@JsonIgnore
	private HttpStatus httpStatus;

	public ErrorResponse(ErrorCodes errorCodes, HttpStatus httpStatus) {
		this.code = errorCodes.getCode();
		this.message = errorCodes.getMessage();
		this.httpStatus = httpStatus;
	}

	public ErrorResponse(ErrorCodes errorCodes) {
		this.code = errorCodes.getCode();
		this.message = errorCodes.getMessage();
	}
}
