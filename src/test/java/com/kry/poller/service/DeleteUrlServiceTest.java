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
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;
import com.kry.poller.util.RequestResponseBuilder;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class DeleteUrlServiceTest {
    @MockBean
    UrlInfoRepository urlInfoRepo;
    @MockBean
    UrlStatusRepository urlStatusRepo;
    @MockBean
    UrlResponseMapper responseMapper;
    @InjectMocks
    DeleteUrlService deleteUrlService;

    @BeforeAll
    public void setUp() {
	MockitoAnnotations.openMocks(this);
	deleteUrlService = new DeleteUrlService(urlInfoRepo, urlStatusRepo, responseMapper);
    }

    @Test
    void deleteUrlByIdTest() {
	Mockito.when(urlStatusRepo.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
	Mockito.when(urlInfoRepo.deleteById(Mockito.anyString())).thenReturn(Mono.empty());
	Mockito.when(responseMapper.buildDeleteResponse()).thenReturn(RequestResponseBuilder.buildDeleteResponse());
	Mono<UrlResponse> response = deleteUrlService.deleteUrlById("2cb25894-14d5-4c49-874f-fbda4bed989e");

	assertNotNull(response);
	StepVerifier.create(response).thenConsumeWhile(result -> {
	    assertNotNull(result);
	    Assert.isTrue("OK".equals(result.getStatus()), "Status not equal");
	    return true;
	}).verifyComplete();

	verify(urlStatusRepo, times(1)).deleteById("2cb25894-14d5-4c49-874f-fbda4bed989e");
	verify(urlInfoRepo, times(1)).deleteById("2cb25894-14d5-4c49-874f-fbda4bed989e");

    }

}
