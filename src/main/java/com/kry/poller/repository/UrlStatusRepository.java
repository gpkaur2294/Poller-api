package com.kry.poller.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kry.poller.entity.UrlStatus;

@Repository
public interface UrlStatusRepository extends ReactiveCrudRepository<UrlStatus, String> {

}
