package com.kry.poller.util;

import java.time.Instant;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.po.AddURLRequest;
import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.ErrorResponse;
import com.kry.poller.po.UpdateUrlRequest;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.po.UrlStatsResponse;

public class RequestResponseBuilder {

    public static AddURLRequest buildAddUrlRequest() {
	AddURLRequest addUrlRequest = new AddURLRequest();
	addUrlRequest.setPollingInterval(1);
	addUrlRequest.setName("kry");
	addUrlRequest.setUrl("https://www.kry.se");
	return addUrlRequest;

    }

    public static AddURLRequest buildAddUrlRequest_invalidUrl() {
	AddURLRequest addUrlRequest = new AddURLRequest();
	addUrlRequest.setPollingInterval(1);
	addUrlRequest.setName("kry");
	addUrlRequest.setUrl("hts://www.kry.se");
	return addUrlRequest;
    }

    public static UrlResponse buildUrlResponse() {
	UrlResponse urlResponse = new UrlResponse();
	urlResponse.setId("2cb25894-14d5-4c49-874f-fbda4bed989e");
	urlResponse.setPollingInterval(1);
	urlResponse.setName("kry");
	urlResponse.setUrl("https://www.kry.se");
	urlResponse.setStatus("OK");
	urlResponse.setAddedAt("2021-04-05T20:25:45.802Z");
	urlResponse.setUpdatedAt("2021-04-05T20:25:45.802Z");
	return urlResponse;
    }

    public static UpdateUrlRequest buildUpdateRequest() {
	UpdateUrlRequest updateUrlRequest = new UpdateUrlRequest();
	updateUrlRequest.setPollingInterval(1);
	updateUrlRequest.setName("kry");
	updateUrlRequest.setUrl("https://www.kry.se");
	updateUrlRequest.setId("2cb25894-14d5-4c49-874f-fbda4bed989e");
	return updateUrlRequest;

    }

    public static UpdateUrlRequest buildUpdateRequest_null() {
	UpdateUrlRequest updateUrlRequest = new UpdateUrlRequest();
	updateUrlRequest.setPollingInterval(1);
	updateUrlRequest.setName("kry");
	updateUrlRequest.setUrl("https://www.kry.se");
	updateUrlRequest.setId(null);
	return updateUrlRequest;

    }

    public static UrlResponse buildDeleteResponse() {
	UrlResponse UrlResponse = new UrlResponse();
	UrlResponse.setStatus("OK");
	return UrlResponse;

    }

    public static UrlInfo buildNewUrlInfo() {
	UrlInfo urlInfo = new UrlInfo();
	urlInfo.setAsNew();
	urlInfo.setId("2cb25894-14d5-4c49-874f-fbda4bed989e");
	urlInfo.setPollingInterval(1);
	urlInfo.setUrl("https://www.kry.se");
	urlInfo.setName("kry");

	return urlInfo;
    }

    public static UrlStatus buildNewUrlStatus() {
	UrlStatus urlStatus = new UrlStatus();
	urlStatus.setAsNew();
	urlStatus.setId("2cb25894-14d5-4c49-874f-fbda4bed989e");
	urlStatus.setStatus("OK");
	return urlStatus;
    }

    public static UrlInfo buildUrlInfo() {
	UrlInfo urlInfo = new UrlInfo();
	urlInfo.setId("2cb25894-14d5-4c49-874f-fbda4bed989e");
	urlInfo.setPollingInterval(1);
	urlInfo.setName("kry");
	urlInfo.setUrl("https://www.kry.se");
	urlInfo.setAddedAt(Instant.parse("2021-04-05T20:25:45.802Z"));
	urlInfo.setUpdatedAt(Instant.parse("2021-04-05T20:25:45.802Z"));
	return urlInfo;
    }

    public static UrlStatus buildUrlStatus() {
	UrlStatus urlStatus = new UrlStatus();
	urlStatus.setId("2cb25894-14d5-4c49-874f-fbda4bed989e");
	urlStatus.setStatus("OK");
	urlStatus.setUpdatedAt(Instant.parse("2021-04-05T20:25:45.802Z"));
	return urlStatus;
    }

    public static UrlStatsResponse buildStatsResponse() {
	UrlStatsResponse urlStatsResponse = new UrlStatsResponse();
	urlStatsResponse.setFailureCount(1);
	urlStatsResponse.setPassedCount(1);
	return urlStatsResponse;
    }

    public static UrlResponse buildUrlResponse_running() {
	UrlResponse UrlResponse = new UrlResponse();
	UrlResponse.setStatus("RUNNING");
	return UrlResponse;
    }

    public static UrlResponse buildUrlErrorResponse() {
	ErrorResponse error = new ErrorResponse(ErrorCodes.INVALID_ID);
	UrlResponse urlResponse = new UrlResponse();
	urlResponse.setError(error);
	return urlResponse;
    }

}
