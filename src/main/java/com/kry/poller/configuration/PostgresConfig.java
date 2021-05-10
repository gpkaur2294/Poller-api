package com.kry.poller.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableR2dbcRepositories
public class PostgresConfig {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
	ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
	initializer.setConnectionFactory(connectionFactory);
	CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
	populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("create.sql")));
	initializer.setDatabasePopulator(populator);
	return initializer;
    }

}
