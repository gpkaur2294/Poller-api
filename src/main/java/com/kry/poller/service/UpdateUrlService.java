package com.kry.poller.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.mapper.UrlRequestMapper;
import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.UpdateUrlRequest;
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
public class UpdateUrlService {
    private final UrlInfoRepository urlInfoRepo;
    private final UrlStatusRepository urlStatusRepo;
    private final InvokeUrlRepository invokeUrlRepo;
    private final UrlResponseMapper responseMapper;
    private final UrlRequestMapper requestMapper;

    /**
     * 1. Update the URL Info based on id
     * 
     * 2. Invoking the URL to get the current status (async)
     * 
     * 3. Update the status of the URL with id (async)
     */
    public Mono<UrlResponse> updateUrl(UpdateUrlRequest urlRequest) {
	return Mono.just(urlRequest).flatMap(this::saveUrlDetails).doOnNext(this::invokeAndSaveStatus)
		.map(responseMapper::buildAddUpdateUrlResponse)
		.defaultIfEmpty(responseMapper.buildErrorResponse(ErrorCodes.INVALID_ID));
    }

    private Mono<UrlInfo> saveUrlDetails(UpdateUrlRequest updateUrlRequest) {
	return urlInfoRepo.findById(updateUrlRequest.getId())
		.flatMap(urlDetailsById -> saveUrlInfo(updateUrlRequest, urlDetailsById));
    }

    private Mono<UrlInfo> saveUrlInfo(UpdateUrlRequest updateUrlRequest, UrlInfo urlDetailsById) {
	return urlInfoRepo.save(requestMapper.buildUpdateUrlInfo(updateUrlRequest, urlDetailsById));
    }

    private void invokeAndSaveStatus(UrlInfo urlInfo) {
	Mono.just(urlInfo)
		.flatMap(req -> invokeUrlRepo.invokeUrl(urlInfo.getUrl()).flatMap(status -> saveUrlStatus(req, status)))
		.subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    private Mono<UrlStatus> saveUrlStatus(UrlInfo req, String status) {
	return urlStatusRepo.save(requestMapper.buildUrlStatus(req, status));
    }

}
