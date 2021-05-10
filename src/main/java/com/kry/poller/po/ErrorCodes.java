package com.kry.poller.po;

public enum ErrorCodes {
	INVALID_URL(203, "Invalid url"), INVALID_REQUEST(204, "Invalid Request"),
	INTERNAL_ERROR(99, "Sorry Something is Wrong"), NO_URL_FOUND(201, "No URLs found"),
	VALID_NAME(203, "Please provide name"), INVALID_ID(202, "Please provide valid ID");

	private int code;
	private String message;

	private ErrorCodes(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
