package com.kry.poller.mapper;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.po.AddURLRequest;
import com.kry.poller.po.UpdateUrlRequest;

@Component
public class UrlRequestMapper {

    public UrlInfo buildUrlInfo(AddURLRequest addRequest) {
	UrlInfo urlInfo = new UrlInfo();
	urlInfo.setId(UUID.randomUUID().toString());
	urlInfo.setAddedAt(Instant.now());
	urlInfo.setName(addRequest.getName());
	urlInfo.setPollingInterval(addRequest.getPollingInterval());
	urlInfo.setUpdatedAt(Instant.now());
	urlInfo.setUrl(addRequest.getUrl());
	return urlInfo;
    }

    public UrlInfo buildUpdateUrlInfo(UpdateUrlRequest updateRequest, UrlInfo urlDetailsById) {
	UrlInfo urlInfo = new UrlInfo();
	urlInfo.setId(updateRequest.getId());
	urlInfo.setName(updateRequest.getName());
	urlInfo.setPollingInterval(updateRequest.getPollingInterval());
	urlInfo.setUpdatedAt(Instant.now());
	urlInfo.setAddedAt(urlDetailsById.getAddedAt());
	urlInfo.setUrl(updateRequest.getUrl());
	return urlInfo;
    }

    public UrlStatus buildUrlStatus(UrlInfo req, String status) {
	UrlStatus urlStatus = new UrlStatus();
	urlStatus.setId(req.getId());
	urlStatus.setStatus(status);
	urlStatus.setUpdatedAt(Instant.now());
	return urlStatus;
    }

}
