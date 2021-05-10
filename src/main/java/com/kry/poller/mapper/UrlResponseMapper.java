package com.kry.poller.mapper;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.ErrorResponse;
import com.kry.poller.po.Status;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.po.UrlStatsResponse;

@Component
public class UrlResponseMapper {

    public UrlResponse buildUrlResponse(UrlInfo urlInfo, UrlStatus status) {
	UrlResponse urlResponse = new UrlResponse();
	urlResponse.setId(urlInfo.getId());
	urlResponse.setName(urlInfo.getName());
	urlResponse.setPollingInterval(urlInfo.getPollingInterval());
	urlResponse.setUrl(urlInfo.getUrl());
	urlResponse.setAddedAt(urlInfo.getAddedAt().toString());
	urlResponse.setUpdatedAt(
		status.getUpdatedAt() != null ? status.getUpdatedAt().toString() : urlInfo.getUpdatedAt().toString());
	urlResponse.setStatus(status.getStatus() != null ? status.getStatus() : Status.RUNNING.name());
	return urlResponse;
    }

    public UrlResponse buildErrorResponse(ErrorCodes errorCode) {
	UrlResponse urlResponse = new UrlResponse();
	urlResponse.setError(new ErrorResponse(errorCode));
	return urlResponse;
    }

    public UrlResponse buildDeleteResponse() {
	UrlResponse urlResponse = new UrlResponse();
	urlResponse.setStatus(Status.OK.name());
	return urlResponse;
    }

    public UrlResponse buildAddUpdateUrlResponse(UrlInfo urlInfo) {
	UrlResponse urlResponse = new UrlResponse();
	urlResponse.setId(urlInfo.getId());
	urlResponse.setName(urlInfo.getName());
	urlResponse.setPollingInterval(urlInfo.getPollingInterval());
	urlResponse.setUrl(urlInfo.getUrl());
	urlResponse.setAddedAt(urlInfo.getAddedAt().toString());
	urlResponse.setUpdatedAt(urlInfo.getUpdatedAt().toString());
	urlResponse.setStatus(Status.RUNNING.name());
	return urlResponse;
    }

    public UrlStatsResponse builStatsResponse(Map<Boolean, Long> countMap) {
	UrlStatsResponse urlStatsResponse = new UrlStatsResponse();
	urlStatsResponse.setPassedCount(countMap.get(Boolean.TRUE));
	urlStatsResponse.setFailureCount(countMap.get(Boolean.FALSE));
	return urlStatsResponse;
    }

}
