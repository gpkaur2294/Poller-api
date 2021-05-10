package com.kry.poller.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kry.poller.entity.UrlInfo;
import com.kry.poller.entity.UrlStatus;
import com.kry.poller.mapper.UrlRequestMapper;
import com.kry.poller.repository.InvokeUrlRepository;
import com.kry.poller.repository.UrlInfoRepository;
import com.kry.poller.repository.UrlStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PollerSchedulerService {
    private final UrlInfoRepository urlInfoRepo;
    private final UrlStatusRepository urlStatusRepo;
    private final InvokeUrlRepository invokeUrlRepo;
    private final UrlRequestMapper requestMapper;

    public Flux<UrlStatus> invokeUrlsAndUpdateStatus() {
	log.info("scheduler running");
	return urlInfoRepo.findAll().parallel().runOn(Schedulers.boundedElastic())
		.flatMap(urlInfoRepo1 -> urlStatusRepo.findById(urlInfoRepo1.getId())
			.filter(filteredstatus -> isPollingRequired(urlInfoRepo1, filteredstatus))
			.flatMap(status -> invokeAndSaveStatus(urlInfoRepo1)))
		.ordered((urlStatus1, urlStatus2) -> urlStatus2.getUpdatedAt().compareTo(urlStatus1.getUpdatedAt()));
    }

    private Mono<UrlStatus> invokeAndSaveStatus(UrlInfo urlInfoRepo1) {
	return invokeUrlRepo.invokeUrl(urlInfoRepo1.getUrl())
		.flatMap(urlResponse -> saveStatus(urlInfoRepo1, urlResponse));
    }

    private Mono<UrlStatus> saveStatus(UrlInfo urlInfoRepo1, String urlResponse) {
	return urlStatusRepo.save(requestMapper.buildUrlStatus(urlInfoRepo1, urlResponse));
    }

    private boolean isPollingRequired(UrlInfo urlInfo, UrlStatus status) {
	return ((Duration.between(status.getUpdatedAt(), Instant.now()).toMinutes()) >= urlInfo.getPollingInterval());
    }

}
