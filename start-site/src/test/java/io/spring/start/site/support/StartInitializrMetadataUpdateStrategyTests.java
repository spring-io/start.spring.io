/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.support;

import java.util.List;

import io.spring.initializr.generator.test.InitializrMetadataTestBuilder;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.InitializrMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Tests for {@link StartInitializrMetadataUpdateStrategy}.
 *
 * @author Stephane Nicoll
 */
class StartInitializrMetadataUpdateStrategyTests {

	private static final JsonMapper jsonMapper = new JsonMapper();

	private RestClient restClient;

	private MockRestServiceServer mockServer;

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		this.mockServer = MockRestServiceServer.bindTo(builder).build();
		this.restClient = builder.build();
	}

	@Test
	void eolVersionsAreRemoved() {
		InitializrMetadata metadata = new InitializrMetadataTestBuilder().addBootVersion("0.0.9.RELEASE", true)
			.addBootVersion("0.0.8.RELEASE", false)
			.build();
		assertThat(metadata.getBootVersions().getDefault().getId()).isEqualTo("0.0.9.RELEASE");
		StartInitializrMetadataUpdateStrategy provider = new StartInitializrMetadataUpdateStrategy(this.restClient,
				jsonMapper);
		expectJson(metadata.getConfiguration().getEnv().getSpringBootMetadataUrl(),
				"metadata/springio/spring-boot.json");
		InitializrMetadata updatedMetadata = provider.update(metadata);
		assertThat(updatedMetadata.getBootVersions()).isNotNull();
		List<DefaultMetadataElement> updatedBootVersions = updatedMetadata.getBootVersions().getContent();
		assertThat(updatedBootVersions).hasSize(4);
		assertBootVersion(updatedBootVersions.get(0), "4.1.1 (SNAPSHOT)", false);
		assertBootVersion(updatedBootVersions.get(1), "4.1.0", true);
		assertBootVersion(updatedBootVersions.get(2), "4.0.8 (SNAPSHOT)", false);
		assertBootVersion(updatedBootVersions.get(3), "4.0.7", false);
	}

	@Test
	void noVersionsAreHandled() {
		StartInitializrMetadataUpdateStrategy provider = new StartInitializrMetadataUpdateStrategy(this.restClient,
				jsonMapper);
		List<DefaultMetadataElement> elements = provider.fetchSpringBootVersions(null);
		assertThat(elements).isNull();
	}

	private static void assertBootVersion(DefaultMetadataElement actual, String name, boolean defaultVersion) {
		assertThat(actual.getName()).isEqualTo(name);
		assertThat(actual.isDefault()).isEqualTo(defaultVersion);
	}

	private void expectJson(String url, String bodyPath) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		this.mockServer.expect(requestTo(url))
			.andExpect(method(HttpMethod.GET))
			.andRespond(withStatus(HttpStatus.OK).body(new ClassPathResource(bodyPath)).headers(httpHeaders));
	}

}
