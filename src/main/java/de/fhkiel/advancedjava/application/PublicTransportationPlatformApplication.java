package de.fhkiel.advancedjava.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "de.fhkiel.advancedjava")
@EnableNeo4jRepositories(basePackages = "de.fhkiel.advancedjava.repository")
@EntityScan(basePackages = "de.fhkiel.advancedjava.entities")
public class PublicTransportationPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicTransportationPlatformApplication.class, args);
	}

}
