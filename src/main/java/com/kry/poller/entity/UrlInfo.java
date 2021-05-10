package com.kry.poller.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("urlinfo")
public class UrlInfo implements Persistable<String> {
    @Id
    private String id;
    private String name;
    private String url;
    private Instant addedAt;
    private Instant updatedAt;
    private int pollingInterval;

    @Transient
    private boolean newUrl;

    @Override
    @Transient
    public boolean isNew() {
	return this.newUrl || id == null;
    }

    public UrlInfo setAsNew() {
	this.newUrl = true;
	return this;
    }
}
