package com.kry.poller.po;

import javax.validation.constraints.NotEmpty;

import com.kry.poller.validator.UrlConstraint;

import lombok.Data;

@Data
public class AddURLRequest {
	@NotEmpty(message = "VALID_NAME")
	private String name;
	@UrlConstraint(message = "INVALID_URL")
	private String url;
	private int pollingInterval;
}
