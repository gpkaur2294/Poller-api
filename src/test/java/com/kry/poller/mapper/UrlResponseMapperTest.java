package com.kry.poller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.po.UrlStatsResponse;
import com.kry.poller.util.RequestResponseBuilder;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class UrlResponseMapperTest {

    @InjectMocks
    UrlResponseMapper urlResponseMapper = new UrlResponseMapper();

    @Test
    void buildUrlResponseTest() {
	UrlResponse result = urlResponseMapper.buildUrlResponse(RequestResponseBuilder.buildUrlInfo(),
		RequestResponseBuilder.buildUrlStatus());
	assertEquals(RequestResponseBuilder.buildUrlResponse().getName(), result.getName());
    }

    @Test
    void buildErrorResponseTest() {
	UrlResponse result = urlResponseMapper.buildErrorResponse(ErrorCodes.INVALID_URL);
	assertEquals(null, result.getName());
    }

    @Test
    void buildDeleteResponseTest() {
	UrlResponse result = urlResponseMapper.buildDeleteResponse();
	assertEquals(RequestResponseBuilder.buildDeleteResponse().getStatus(), result.getStatus());
    }

    @Test
    void buildAddUpdateUrlResponseTest() {
	UrlResponse result = urlResponseMapper.buildAddUpdateUrlResponse(RequestResponseBuilder.buildUrlInfo());
	assertEquals(RequestResponseBuilder.buildUrlResponse().getName(), result.getName());
    }

    @Test
    void builStatsResponseTest() {
	Map<Boolean, Long> countMap = new HashMap<>();
	countMap.put(Boolean.TRUE, 1L);
	countMap.put(Boolean.FALSE, 1L);
	UrlStatsResponse result = urlResponseMapper.builStatsResponse(countMap);
	assertEquals(RequestResponseBuilder.buildStatsResponse().getPassedCount(), result.getPassedCount());
    }

}
