package com.kry.poller.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("urlstatus")
public class UrlStatus implements Persistable<String> {

    @Id
    private String id;
    private String status;
    private Instant updatedAt;

    @Transient
    private boolean newStatus;

    @Override
    @Transient
    public boolean isNew() {
	return this.newStatus || id == null;
    }

    public UrlStatus setAsNew() {
	this.newStatus = true;
	return this;
    }

}
