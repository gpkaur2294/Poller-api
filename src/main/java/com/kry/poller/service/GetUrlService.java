package com.kry.poller.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.ErrorCodes;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;
import com.kry.poller.util.PollerException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class GetUrlService {

    private final UrlInfoRepository urlInfoRepo;
    private final UrlStatusRepository urlStatusRepo;
    private final UrlResponseMapper responseMapper;

    /** Gets the URLInfo and url status for all records */
    public Flux<UrlResponse> getAllUrlStatus() {
	return urlInfoRepo.findAll()
		.flatMap(url -> getUrlStatus(url).map(statusInfo -> responseMapper.buildUrlResponse(url, statusInfo)))
		.switchIfEmpty(Mono.defer(this::handleNoUrl));
    }

    private Mono<UrlResponse> handleNoUrl() {
	return Mono.error(new PollerException(ErrorCodes.NO_URL_FOUND, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private Mono<UrlStatus> getUrlStatus(UrlInfo url) {
	return urlStatusRepo.findById(url.getId()).defaultIfEmpty(new UrlStatus());
    }

}
