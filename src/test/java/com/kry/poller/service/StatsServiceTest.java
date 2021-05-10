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

import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.UrlStatsResponse;
import com.kry.poller.repository.UrlStatusRepository;
import com.kry.poller.util.RequestResponseBuilder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class StatsServiceTest {
    @InjectMocks
    StatsService statsService;

    @MockBean
    UrlStatusRepository urlStatusRepo;
    @MockBean
    UrlResponseMapper responseMapper;

    @BeforeAll
    public void setUp() {
	statsService = new StatsService(urlStatusRepo, responseMapper);
    }

    @Test
    void getUrlStatsTest() {
	Mockito.when(urlStatusRepo.findAll()).thenReturn(Flux.just(RequestResponseBuilder.buildUrlStatus()));
	Mockito.when(responseMapper.builStatsResponse(Mockito.anyMap()))
		.thenReturn(RequestResponseBuilder.buildStatsResponse());
	Mono<UrlStatsResponse> response = statsService.getUrlStats();

	assertNotNull(response);
	StepVerifier.create(response).thenConsumeWhile(result -> {
	    assertNotNull(result);
	    Assert.isTrue(1 == result.getPassedCount(), "Pass count not equal");
	    Assert.isTrue(1 == result.getFailureCount(), "Fail count not equal");
	    return true;
	}).verifyComplete();

	verify(urlStatusRepo, times(1)).findAll();

    }
}
