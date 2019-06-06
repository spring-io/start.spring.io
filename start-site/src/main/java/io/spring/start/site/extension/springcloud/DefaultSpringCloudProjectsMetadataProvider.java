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

package io.spring.start.site.extension.springcloud;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * The default {@link SpringCloudProjectsMetadataProvider} implementation that caches the
 * {@link SpringCloudProjectsMetadata}.
 *
 * @author Olga Maciaszek-Sharma
 */
class DefaultSpringCloudProjectsMetadataProvider
		implements SpringCloudProjectsMetadataProvider {

	private static final Log LOG = LogFactory
			.getLog(DefaultSpringCloudProjectsMetadataProvider.class);

	private final RestTemplate restTemplate;

	private final String infoHost;

	DefaultSpringCloudProjectsMetadataProvider(RestTemplate restTemplate,
			SpringCloudProjectGenerationProperties properties) {

		this.restTemplate = restTemplate;
		this.infoHost = properties.getInfoHost();
	}

	@Override
	@Cacheable(value = "start.spring-cloud-projects-metadata", unless = "#result == null")
	public SpringCloudProjectsMetadata get(String releaseTrainVersion) {
		Map<String, String> projects = getProjects(releaseTrainVersion);
		if (projects.size() > 0) {
			return new SpringCloudProjectsMetadata(releaseTrainVersion, projects);
		}
		return null;
	}

	private Map<String, String> getProjects(String releaseTrainVersion) {
		try {
			LOG.info(
					"Fetching Spring Cloud projects metadata for " + releaseTrainVersion);
			ResponseEntity<HashMap<String, String>> springCloudProjectsResponse = this.restTemplate
					.exchange(this.infoHost + releaseTrainVersion, HttpMethod.GET, null,
							new ParameterizedTypeReference<HashMap<String, String>>() {
							});
			return springCloudProjectsResponse.getBody();
		}
		catch (Exception exception) {
			LOG.warn("Failed to fetch Spring Cloud projects metadata for "
					+ releaseTrainVersion, exception);
		}
		return Collections.emptyMap();
	}

}
