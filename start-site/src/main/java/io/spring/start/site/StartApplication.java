/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.container.SimpleDockerServiceResolver;
import io.spring.start.site.project.ProjectDescriptionCustomizerConfiguration;
import io.spring.start.site.support.CacheableMavenVersionResolver;
import io.spring.start.site.support.StartInitializrMetadataUpdateStrategy;
import io.spring.start.site.web.HomeController;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

/**
 * Initializr website application.
 *
 * @author Stephane Nicoll
 */
@EnableAutoConfiguration
@SpringBootConfiguration
@Import(ProjectDescriptionCustomizerConfiguration.class)
@EnableCaching
@EnableAsync
@EnableConfigurationProperties(StartConfigurationProperties.class)
public class StartApplication {

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}

	@Bean
	public HomeController homeController() {
		return new HomeController();
	}

	@Bean
	public StartInitializrMetadataUpdateStrategy initializrMetadataUpdateStrategy(RestClient.Builder restClientBuilder,
			JsonMapper jsonMapper) {
		RestClient restClient = restClientBuilder.defaultHeader(HttpHeaders.USER_AGENT, "start.spring.io").build();
		return new StartInitializrMetadataUpdateStrategy(restClient, jsonMapper);
	}

	@Bean
	public CacheableMavenVersionResolver mavenVersionResolver(StartConfigurationProperties properties)
			throws IOException {
		Path location;
		if (StringUtils.hasText(properties.getMavenVersionResolver().getCacheDirectory())) {
			location = Path.of(properties.getMavenVersionResolver().getCacheDirectory());
		}
		else {
			location = Files.createTempDirectory("version-resolver-cache-");
		}
		return new CacheableMavenVersionResolver(MavenVersionResolver.withCacheLocation(location));
	}

	@Bean
	public SimpleDockerServiceResolver dockerServiceResolver() {
		return new SimpleDockerServiceResolver();
	}

}
