package com.kry.poller.util;

import java.util.Map;

import javax.validation.ConstraintDefinitionException;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.ErrorResponse;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final String ERROR = "error";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
	Map<String, Object> map = super.getErrorAttributes(request, options);

	if (getError(request) instanceof PollerException) {
	    PollerException exception = (PollerException) getError(request);
	    map.put(ERROR, new ErrorResponse(exception.getErrorCode(), exception.getStatus()));
	} else if (getError(request) instanceof WebExchangeBindException) {
	    WebExchangeBindException exception = (WebExchangeBindException) getError(request);
	    map.put(ERROR, new ErrorResponse(ErrorCodes.valueOf(exception.getFieldError().getDefaultMessage()),
		    HttpStatus.BAD_REQUEST));
	} else if (getError(request) instanceof ConstraintDefinitionException) {
	    ConstraintDefinitionException exception = (ConstraintDefinitionException) getError(request);
	    map.put(ERROR, new ErrorResponse(ErrorCodes.valueOf(exception.getMessage()), HttpStatus.BAD_REQUEST));
	} else {
	    map.put(ERROR, new ErrorResponse(ErrorCodes.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
	}
	return map;
    }

}
