package com.kry.poller.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kry.poller.po.AddURLRequest;
import com.kry.poller.po.UpdateUrlRequest;
import com.kry.poller.po.UrlResponse;
import com.kry.poller.po.UrlStatsResponse;
import com.kry.poller.service.AddUrlService;
import com.kry.poller.service.DeleteUrlService;
import com.kry.poller.service.GetUrlService;
import com.kry.poller.service.StatsService;
import com.kry.poller.service.UpdateUrlService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/poller")
@CrossOrigin
@RequiredArgsConstructor
public class UrlContoller {
    private final GetUrlService getService;
    private final AddUrlService addService;
    private final UpdateUrlService updateService;
    private final DeleteUrlService deleteService;
    private final StatsService statsService;

    @PostMapping("/url/add")
    @Operation(tags = "1.Add Url ", description = "Adds new Url Info and status", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    public Mono<UrlResponse> addUrl(@Valid @RequestBody AddURLRequest urlRequest) {
	return addService.addUrl(urlRequest);
    }

    @PostMapping("/url/update")
    @Operation(tags = "2.Update Url ", description = "Update the UrlInfo based on ID and status", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody())
    public Mono<UrlResponse> updateUrlById(@Valid @RequestBody UpdateUrlRequest urlRequest) {
	return updateService.updateUrl(urlRequest);
    }

    @GetMapping("/urls")
    @Operation(tags = "3.Get Url", description = "Gets the Url Information with status")
    public Flux<UrlResponse> getAllUrls() {
	return getService.getAllUrlStatus();
    }

    @DeleteMapping("/url/{id}/delete")
    @Operation(tags = "4.Delete Url ", description = "Deletes the Url details based on ID", parameters = {
	    @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "ID of the Url Info") })
    public Mono<UrlResponse> deleteUrlById(@PathVariable String id) {
	return deleteService.deleteUrlById(id);

    }

    @GetMapping("/stats")
    @Operation(tags = "5.Get Stats", description = "Gets the Url Stats based on number of OK/Fail status")
    public Mono<UrlStatsResponse> getUrlStats() {
	return statsService.getUrlStats();
    }
}
