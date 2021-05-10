package com.kry.poller.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.util.RequestResponseBuilder;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class UrlRequestMapperTest {
    @InjectMocks
    UrlRequestMapper urlRequestMapper = new UrlRequestMapper();

    @Test
    void buildUrlInfoTest() {
	UrlInfo result = urlRequestMapper.buildUrlInfo(RequestResponseBuilder.buildAddUrlRequest());
	assertEquals(RequestResponseBuilder.buildUrlInfo().getName(), result.getName());
    }

    @Test
    void buildUpdateUrlInfo() {
	UrlInfo result = urlRequestMapper.buildUpdateUrlInfo(RequestResponseBuilder.buildUpdateRequest(),
		RequestResponseBuilder.buildUrlInfo());
	assertEquals(RequestResponseBuilder.buildUrlInfo().getName(), result.getName());

    }

    @Test
    void buildUrlStatusTest() {
	UrlStatus result = urlRequestMapper.buildUrlStatus(RequestResponseBuilder.buildUrlInfo(), "OK");
	assertEquals(RequestResponseBuilder.buildUrlStatus().getStatus(), result.getStatus());
    }
}
