package com.kry.poller.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.kry.poller.entity.UrlInfo;

@Repository
public interface UrlInfoRepository extends ReactiveCrudRepository<UrlInfo, String> {

}
