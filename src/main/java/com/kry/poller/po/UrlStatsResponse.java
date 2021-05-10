package com.kry.poller.po;

import lombok.Data;

@Data
public class UrlStatsResponse {
	private long passedCount;
	private long failureCount;
}
