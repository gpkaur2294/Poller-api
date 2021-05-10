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
import com.kry.poller.mapper.UrlRequestMapper;
import com.kry.poller.repository.InvokeUrlRepository;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;
import com.kry.poller.util.RequestResponseBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class PollerSchedulerServiceTest {
    @MockBean
    UrlInfoRepository urlInfoRepo;
    @MockBean
    UrlStatusRepository urlStatusRepo;
    @MockBean
    InvokeUrlRepository invokeUrlRepo;
    @MockBean
    UrlRequestMapper requestMapper;
    @InjectMocks
    PollerSchedulerService pollerSchedulerService;

    @BeforeAll
    public void setUp() {
	pollerSchedulerService = new PollerSchedulerService(urlInfoRepo, urlStatusRepo, invokeUrlRepo, requestMapper);
    }

    @Test
    void invokeUrlsAndUpdateStatusTest() {
	Mockito.when(urlInfoRepo.findAll()).thenReturn(Flux.just(RequestResponseBuilder.buildUrlInfo()));
	Mockito.when(urlStatusRepo.findById(Mockito.anyString()))
		.thenReturn(Mono.just(RequestResponseBuilder.buildUrlStatus()));

	Mockito.when(invokeUrlRepo.invokeUrl(Mockito.anyString())).thenReturn(Mono.just("OK"));

	Mockito.when(requestMapper.buildUrlStatus(Mockito.any(), Mockito.anyString()))
		.thenReturn(RequestResponseBuilder.buildUrlStatus());

	Mockito.when(urlStatusRepo.save(Mockito.any())).thenReturn(Mono.just(RequestResponseBuilder.buildUrlStatus()));

	Flux<UrlStatus> response = pollerSchedulerService.invokeUrlsAndUpdateStatus();

	assertNotNull(response);
	StepVerifier.create(response).thenConsumeWhile(result -> {
	    assertNotNull(result);
	    Assert.isTrue("OK".equals(result.getStatus()), "status not equal");
	    return true;
	}).verifyComplete();

	verify(urlInfoRepo, times(1)).findAll();
	verify(urlStatusRepo, times(1)).findById("2cb25894-14d5-4c49-874f-fbda4bed989e");

    }

}
