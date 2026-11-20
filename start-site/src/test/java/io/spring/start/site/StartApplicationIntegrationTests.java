/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site;

import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataBuilder;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.cache.test.autoconfigure.AutoConfigureCache;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * General integration tests for {@link StartApplication}.
 *
 * @author Stephane Nicoll
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureCache
@AutoConfigureRestTestClient
class StartApplicationIntegrationTests {

	@Autowired
	private RestTestClient restClient;

	@Autowired
	private InitializrMetadataProvider metadataProvider;

	@Test
	void metadataCanBeSerialized() {
		String body = this.restClient.get()
			.uri("/")
			.accept(MediaType.parseMediaType("application/vnd.initializr.v2.1+json"))
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(String.class)
			.returnResult()
			.getResponseBody();
		new JsonMapper().readTree(body);
	}

	@Test
	void configurationCanBeSerialized() {
		byte[] body = this.restClient.get()
			.uri("/metadata/config")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(byte[].class)
			.returnResult()
			.getResponseBody();
		InitializrMetadata actual = InitializrMetadataBuilder.create()
			.withInitializrMetadata(new ByteArrayResource(body))
			.build();
		assertThat(actual).isNotNull();
		InitializrMetadata expected = this.metadataProvider.get();
		assertThat(actual.getDependencies().getAll().size()).isEqualTo(expected.getDependencies().getAll().size());
		assertThat(actual.getConfiguration().getEnv().getBoms().size())
			.isEqualTo(expected.getConfiguration().getEnv().getBoms().size());
	}

}
