package com.kry.poller.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.kry.poller.po.ErrorResponse;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes, Resources resources,
	    ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
	super(errorAttributes, resources, applicationContext);
	super.setMessageWriters(serverCodecConfigurer.getWriters());
	super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
	return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
	Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());
	Set<String> set = getKeystoRemove();
	errorPropertiesMap.keySet().removeAll(set);
	return ServerResponse.status(((ErrorResponse) errorPropertiesMap.get("error")).getHttpStatus())
		.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(errorPropertiesMap));
    }

    private Set<String> getKeystoRemove() {
	Set<String> set = new HashSet<>();
	set.add("timestamp");
	set.add("requestId");
	set.add("status");
	set.add("path");
	set.add("message");
	set.add("code");
	return set;
    }
}
