package com.kry.poller.po;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class UrlResponse {
	private String name;
	private String url;
	private Integer pollingInterval;
	private String addedAt;
	private String updatedAt;
	private ErrorResponse error;
	private String status;
	private String id;
}
