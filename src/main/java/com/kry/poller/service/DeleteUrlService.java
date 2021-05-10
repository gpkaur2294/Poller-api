package com.kry.poller.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteUrlService {
    private final UrlInfoRepository urlInfoRepo;
    private final UrlStatusRepository urlStatusRepo;
    private final UrlResponseMapper responseMapper;

    /** Deletes the URLInfo and its status info from DB */
    public Mono<UrlResponse> deleteUrlById(String id) {
	return urlStatusRepo.deleteById(id).then(urlInfoRepo.deleteById(id))
		.thenReturn(responseMapper.buildDeleteResponse());
    }

}
