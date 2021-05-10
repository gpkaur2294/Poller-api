package com.kry.poller.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import com.kry.poller.entity.UrlStatus;
import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;
import com.kry.poller.util.RequestResponseBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class GetUrlServiceTest {

    @InjectMocks
    GetUrlService getUrlService;

    @MockBean
    UrlInfoRepository urlInfoRepo;
    @MockBean
    UrlStatusRepository urlStatusRepo;
    @MockBean
    UrlResponseMapper responseMapper;

    @BeforeAll
    public void setUp() {
	getUrlService = new GetUrlService(urlInfoRepo, urlStatusRepo, responseMapper);
    }

    @Test
    void getAllUrlStatusTest() {
	Mockito.when(urlInfoRepo.findAll()).thenReturn(Flux.just(RequestResponseBuilder.buildUrlInfo()));
	Mockito.when(urlStatusRepo.findById(Mockito.anyString()))
		.thenReturn(Mono.just(RequestResponseBuilder.buildUrlStatus()));
	Mockito.when(responseMapper.buildUrlResponse(Mockito.any(), Mockito.any()))
		.thenReturn(RequestResponseBuilder.buildUrlResponse());
	Flux<UrlResponse> response = getUrlService.getAllUrlStatus();
	assertNotNull(response);
	StepVerifier.create(response).thenConsumeWhile(result -> {
	    assertNotNull(result);
	    Assert.isTrue("kry".equals(result.getName()), "Url name not equal");
	    Assert.isTrue("https://www.kry.se".equals(result.getUrl()), "Url not equal");
	    return true;
	}).verifyComplete();

	verify(urlInfoRepo, times(1)).findAll();
	verify(urlStatusRepo, times(1)).findById("2cb25894-14d5-4c49-874f-fbda4bed989e");
    }

    @Test
    void getAllUrlStatusTest_running() {
	Mockito.when(urlInfoRepo.findAll()).thenReturn(Flux.just(RequestResponseBuilder.buildUrlInfo()));
	Mockito.when(urlStatusRepo.findById(Mockito.anyString())).thenReturn(Mono.just(new UrlStatus()));
	Mockito.when(responseMapper.buildUrlResponse(Mockito.any(), Mockito.any()))
		.thenReturn(RequestResponseBuilder.buildUrlResponse_running());
	Flux<UrlResponse> response = getUrlService.getAllUrlStatus();
	assertNotNull(response);
	StepVerifier.create(response).thenConsumeWhile(result -> {
	    assertNotNull(result);
	    Assert.isTrue("RUNNING".equals(result.getStatus()), "Status is incorrect");
	    return true;
	}).verifyComplete();

	verify(urlInfoRepo, times(1)).findAll();
	verify(urlStatusRepo, times(1)).findById("2cb25894-14d5-4c49-874f-fbda4bed989e");
    }

    @Test
    void getAllUrlStatusTest_NoUrl() {
	Mockito.when(urlInfoRepo.findAll()).thenReturn(Flux.empty());
	Mockito.when(urlStatusRepo.findById(Mockito.anyString())).thenReturn(Mono.empty());
	Mockito.when(responseMapper.buildUrlResponse(Mockito.any(), Mockito.any()))
		.thenReturn(RequestResponseBuilder.buildUrlResponse_running());
	Flux<UrlResponse> response = getUrlService.getAllUrlStatus();
	StepVerifier.create(response).expectErrorMessage(ErrorCodes.NO_URL_FOUND.getMessage()).verify();
	verify(responseMapper, times(0)).buildUrlResponse(RequestResponseBuilder.buildUrlInfo(),
		RequestResponseBuilder.buildUrlStatus());
	verify(urlStatusRepo, times(0)).findById("2cb25894-14d5-4c49-874f-fbda4bed989e");
    }
}
