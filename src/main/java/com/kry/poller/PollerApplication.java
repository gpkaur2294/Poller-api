package com.kry.poller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableWebFlux
@EnableScheduling
@OpenAPIDefinition(info = @Info(title = "Poller Application", version = "0.1", description = "Poller application can save,update,delete and get the URL info saved by the User./n User can also get the stats. There is a poller running in the background to check the URL status based/n on the interval configured."))
public class PollerApplication {

    public static void main(String[] args) {
	SpringApplication.run(PollerApplication.class, args);
    }

}
