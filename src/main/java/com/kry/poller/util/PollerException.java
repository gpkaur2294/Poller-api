package com.kry.poller.util;

import org.springframework.http.HttpStatus;

import com.kry.poller.po.ErrorCodes;

public class PollerException extends Exception {

    private static final long serialVersionUID = 7718828512143293558L;
    private final HttpStatus httpstatus;
    private final ErrorCodes errorCodes;

    public PollerException(ErrorCodes error, HttpStatus httpstatus) {
	super(error.getMessage());
	this.httpstatus = httpstatus;
	this.errorCodes = error;
    }

    public HttpStatus getStatus() {
	return this.httpstatus;
    }

    public ErrorCodes getErrorCode() {
	return this.errorCodes;
    }
}
