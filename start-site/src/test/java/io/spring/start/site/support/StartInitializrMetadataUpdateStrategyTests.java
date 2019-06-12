/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.initializr.generator.spring.test.InitializrMetadataTestBuilder;
import io.spring.initializr.metadata.InitializrMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * Tests for {@link StartInitializrMetadataUpdateStrategy}.
 *
 * @author Stephane Nicoll
 */
class StartInitializrMetadataUpdateStrategyTests {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@BeforeEach
	public void setUp() {
		this.restTemplate = new RestTemplate();
		this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
	}

	@Test
	void metadataUpdateStrategyIgnoreServerFailures() {
		InitializrMetadata metadata = new InitializrMetadataTestBuilder().addBootVersion("2.2.0.RELEASE", true)
				.addBootVersion("2.1.9.RELEASE", false).build();
		this.mockServer.expect(requestTo(metadata.getConfiguration().getEnv().getSpringBootMetadataUrl()))
				.andExpect(method(HttpMethod.GET)).andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
		InitializrMetadata updatedMetadata = new StartInitializrMetadataUpdateStrategy(this.restTemplate, objectMapper)
				.update(metadata);

	}

}
