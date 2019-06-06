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

import javax.cache.CacheManager;

import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrProperties;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudProjectsMetadataProvider}.
 *
 * @author Olga Maciaszek-Sharma
 */
@SpringBootTest
class DefaultSpringCloudProjectsMetadataProviderTests {

	@Autowired
	private SpringCloudProjectsMetadataProvider springCloudProjectsMetadataProvider;

	@Autowired
	private InitializrProperties initializrProperties;

	@Autowired
	private CacheManager cacheManager;

	@Test
	void getAndCacheSpringCloudProjectVersions() {
		String releaseTrainVersion = getValidReleaseTrainVersion();

		SpringCloudProjectsMetadata metadata = this.springCloudProjectsMetadataProvider
				.get(releaseTrainVersion);

		assertThat(metadata).isNotNull();
		assertThat(metadata.getReleaseTrainVersion()).isEqualTo(releaseTrainVersion);
		assertThat(metadata.getProjects()).isNotEmpty();
		assertThat(this.cacheManager.getCache("start.spring-cloud-projects-metadata")
				.get(releaseTrainVersion)).isNotNull();
	}

	@Test
	void returnNullAndDoNotCacheForInvalidReleaseVersion() {
		String releaseTrainVersion = "Invalid.VERSION";

		SpringCloudProjectsMetadata metadata = this.springCloudProjectsMetadataProvider
				.get(releaseTrainVersion);

		assertThat(metadata).isNull();
		assertThat(this.cacheManager.getCache("start.spring-cloud-projects-metadata")
				.get(releaseTrainVersion)).isNull();
	}

	private String getValidReleaseTrainVersion() {
		return this.initializrProperties.getEnv().getBoms().get("spring-cloud")
				.getMappings().stream().map((BillOfMaterials.Mapping::getVersion))
				.filter((version) -> !version.contains("SNAPSHOT"))
				.reduce((first, second) -> second)
				.orElseThrow(() -> new IllegalArgumentException(
						"Release train version not found."));
	}

}
