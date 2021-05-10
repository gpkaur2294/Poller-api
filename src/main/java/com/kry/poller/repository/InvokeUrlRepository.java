package com.kry.poller.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.kry.poller.po.Status;

import reactor.core.publisher.Mono;

@Repository
public class InvokeUrlRepository {

    @Autowired
    @Qualifier("webClientP")
    private WebClient webClient;

    public Mono<String> invokeUrl(String url) {
	return webClient.get().uri(url).retrieve().toEntity(String.class)
		.filter(response -> response.getStatusCode().equals(HttpStatus.OK)).map(res -> Status.OK.name())
		.defaultIfEmpty(Status.FAIL.name()).onErrorReturn(Status.FAIL.name());
    }

}
