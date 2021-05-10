package com.kry.poller.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
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

import com.kry.poller.mapper.UrlRequestMapper;
import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.repository.InvokeUrlRepository;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;
import com.kry.poller.util.RequestResponseBuilder;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class AddUrlServiceTest {

    @MockBean
    UrlInfoRepository urlInfoRepo;
    @MockBean
    UrlStatusRepository urlStatusRepo;
    @MockBean
    InvokeUrlRepository invokeUrlRepo;
    @MockBean
    UrlResponseMapper responseMapper;
    @MockBean
    UrlRequestMapper requestMapper;
    @InjectMocks
    AddUrlService addUrlService;

    @BeforeAll
    public void setUp() {
	MockitoAnnotations.openMocks(this);
	addUrlService = new AddUrlService(urlInfoRepo, urlStatusRepo, invokeUrlRepo, responseMapper, requestMapper);
    }

    @Test
    public void addUrlTest() {

	Mockito.when(urlInfoRepo.save(Mockito.any())).thenReturn(Mono.just(RequestResponseBuilder.buildNewUrlInfo()));
	Mockito.when(requestMapper.buildUrlInfo(Mockito.any())).thenReturn(RequestResponseBuilder.buildNewUrlInfo());

	Mockito.when(invokeUrlRepo.invokeUrl(Mockito.anyString())).thenReturn(Mono.just("OK"));
	Mockito.when(urlStatusRepo.save(Mockito.any()))
		.thenReturn(Mono.just(RequestResponseBuilder.buildNewUrlStatus()));
	Mockito.when(requestMapper.buildUrlStatus(Mockito.any(), Mockito.any()))
		.thenReturn(RequestResponseBuilder.buildNewUrlStatus());

	Mockito.when(responseMapper.buildAddUpdateUrlResponse(Mockito.any()))
		.thenReturn(RequestResponseBuilder.buildUrlResponse());

	Mono<UrlResponse> response = addUrlService.addUrl(RequestResponseBuilder.buildAddUrlRequest());

	assertNotNull(response);
	StepVerifier.create(response).thenConsumeWhile(result -> {
	    assertNotNull(result);
	    Assert.isTrue("kry".equals(result.getName()), "Url name not equal");
	    Assert.isTrue("https://www.kry.se".equals(result.getUrl()), "Url not equal");
	    return true;
	}).verifyComplete();

	verify(urlInfoRepo, times(1)).save(RequestResponseBuilder.buildNewUrlInfo());
	verify(urlStatusRepo, timeout(1000).times(1)).save(RequestResponseBuilder.buildNewUrlStatus());
	verify(invokeUrlRepo, timeout(1000).times(1)).invokeUrl("https://www.kry.se");

    }

}
