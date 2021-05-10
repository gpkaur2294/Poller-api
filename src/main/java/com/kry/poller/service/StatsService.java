package com.kry.poller.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.poller.mapper.UrlResponseMapper;
import com.kry.poller.po.Status;
import com.kry.poller.po.UrlStatsResponse;
import com.kry.poller.repository.UrlStatusRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Transactional
public class StatsService {

    private final UrlStatusRepository urlStatusRepo;
    private final UrlResponseMapper responseMapper;

    /** Get and calculate the URL stats based on number of ok and fail */
    public Mono<UrlStatsResponse> getUrlStats() {
	return urlStatusRepo.findAll().collect(Collectors
		.partitioningBy(status -> Status.OK.name().equalsIgnoreCase(status.getStatus()), Collectors.counting()))
		.map(responseMapper::builStatsResponse);
    }

}
