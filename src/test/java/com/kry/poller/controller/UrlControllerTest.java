package com.kry.poller.controller;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.kry.poller.service.AddUrlService;
import com.kry.poller.service.DeleteUrlService;
import com.kry.poller.service.GetUrlService;
import com.kry.poller.service.StatsService;
import com.kry.poller.service.UpdateUrlService;
import com.kry.poller.util.GlobalErrorAttributes;
import com.kry.poller.util.RequestResponseBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = UrlContoller.class)
@Import({ GlobalErrorAttributes.class })
class UrlControllerTest {
    @Autowired
    WebTestClient webClient;
    @MockBean
    GetUrlService getService;
    @MockBean
    AddUrlService addService;
    @MockBean
    UpdateUrlService updateService;
    @MockBean
    DeleteUrlService deleteService;
    @MockBean
    StatsService statsService;

    @Test
    void getAllUrlsTest() {
	Mockito.when(getService.getAllUrlStatus()).thenReturn(Flux.just(RequestResponseBuilder.buildUrlResponse()));
	webClient.get().uri("/poller/urls").exchange().expectStatus().isOk().expectBody().jsonPath("$.[0].url")
		.isEqualTo("https://www.kry.se").jsonPath("$.[0].name").isEqualTo("kry");
	Mockito.verify(getService, times(1)).getAllUrlStatus();
    }

    @Test
    void addUrlTest() {
	Mockito.when(addService.addUrl(Mockito.any())).thenReturn(Mono.just(RequestResponseBuilder.buildUrlResponse()));
	webClient.post().uri("/poller/url/add").contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(RequestResponseBuilder.buildAddUrlRequest())).exchange().expectStatus()
		.isOk().expectBody().jsonPath("$.url").isEqualTo("https://www.kry.se").jsonPath("$.name")
		.isEqualTo("kry");
	Mockito.verify(addService, times(1)).addUrl(RequestResponseBuilder.buildAddUrlRequest());
    }

    @Test
    void addUrlInvalidTest() {
	Mockito.when(addService.addUrl(Mockito.any())).thenReturn(Mono.just(RequestResponseBuilder.buildUrlResponse()));
	webClient.post().uri("/poller/url/add").contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(RequestResponseBuilder.buildAddUrlRequest_invalidUrl())).exchange()
		.expectStatus().is4xxClientError();
	Mockito.verify(addService, times(0)).addUrl(RequestResponseBuilder.buildAddUrlRequest());
    }

    @Test
    void updateUrlByIdTest() {
	Mockito.when(updateService.updateUrl(Mockito.any()))
		.thenReturn(Mono.just(RequestResponseBuilder.buildUrlResponse()));
	webClient.post().uri("/poller/url/update").contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(RequestResponseBuilder.buildUpdateRequest())).exchange().expectStatus()
		.isOk().expectBody().jsonPath("$.url").isEqualTo("https://www.kry.se").jsonPath("$.name")
		.isEqualTo("kry");
	Mockito.verify(updateService, times(1)).updateUrl(RequestResponseBuilder.buildUpdateRequest());
    }

    @Test
    void updateUrlInvalidTest() {
	Mockito.when(updateService.updateUrl(Mockito.any()))
		.thenReturn(Mono.just(RequestResponseBuilder.buildUrlResponse()));
	webClient.post().uri("/poller/url/update").contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(RequestResponseBuilder.buildUpdateRequest_null())).exchange()
		.expectStatus().is4xxClientError();
	Mockito.verify(updateService, times(0)).updateUrl(RequestResponseBuilder.buildUpdateRequest_null());
    }

    @Test
    void deleteUrlByIdTest() {
	Mockito.when(deleteService.deleteUrlById(Mockito.anyString()))
		.thenReturn(Mono.just(RequestResponseBuilder.buildDeleteResponse()));
	webClient.delete().uri("/poller/url/2cb25894-14d5-4c49-874f-fbda4bed989e/delete").exchange().expectStatus()
		.isOk();
	Mockito.verify(deleteService, times(1)).deleteUrlById("2cb25894-14d5-4c49-874f-fbda4bed989e");
    }

    @Test
    void getUrlStatsTest() {
	Mockito.when(statsService.getUrlStats()).thenReturn(Mono.just(RequestResponseBuilder.buildStatsResponse()));
	webClient.get().uri("/poller/stats").exchange().expectStatus().isOk();
	Mockito.verify(statsService, times(1)).getUrlStats();
    }

}
