package com.kry.poller.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kry.poller.service.PollerSchedulerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PollerScheduler {
    private final PollerSchedulerService schedulerService;

    @Scheduled(cron = "0 0/2 * * * *")
    public void scheduleInvokeUrl() {
	schedulerService.invokeUrlsAndUpdateStatus().subscribe();
    }

}
