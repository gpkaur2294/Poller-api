package com.kry.poller.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.mapper.UrlRequestMapper;
import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.AddURLRequest;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.repository.InvokeUrlRepository;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Transactional
public class AddUrlService {
    private final UrlInfoRepository urlInfoRepo;
    private final UrlStatusRepository urlStatusRepo;
    private final InvokeUrlRepository invokeUrlRepo;
    private final UrlResponseMapper responseMapper;
    private final UrlRequestMapper requestMapper;

    /**
     * 1. Save the URL Info
     * 
     * 2. Invoking the URL to get the current status (async)
     * 
     * 3. Update the status of the URL (async)
     */

    public Mono<UrlResponse> addUrl(AddURLRequest addRequest) {
	return Mono.just(addRequest).flatMap(req -> saveUrlInfo(addRequest)).doOnNext(this::invokeAndSaveStatus)
		.map(responseMapper::buildAddUpdateUrlResponse);
    }

    private Mono<UrlInfo> saveUrlInfo(AddURLRequest addRequest) {
	return urlInfoRepo.save(requestMapper.buildUrlInfo(addRequest).setAsNew());
    }

    private void invokeAndSaveStatus(UrlInfo urlInfo) {
	Mono.just(urlInfo)
		.flatMap(req -> invokeUrlRepo.invokeUrl(urlInfo.getUrl()).flatMap(status -> saveStatus(req, status)))
		.subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    private Mono<UrlStatus> saveStatus(UrlInfo req, String status) {
	return urlStatusRepo.save(requestMapper.buildUrlStatus(req, status).setAsNew());
    }

}
