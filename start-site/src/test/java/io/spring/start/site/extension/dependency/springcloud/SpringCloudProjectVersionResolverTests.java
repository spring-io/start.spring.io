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

package io.spring.start.site.extension.dependency.springcloud;

import java.util.Collections;

import io.spring.initializr.generator.test.InitializrMetadataTestBuilder;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringCloudProjectVersionResolver}.
 *
 * @author Stephane Nicoll
 */
class SpringCloudProjectVersionResolverTests {

	private final DependencyManagementVersionResolver versionResolver = mock(DependencyManagementVersionResolver.class);

	@Test
	void resolveWithNoSpringCloudBom() {
		BillOfMaterials bom = BillOfMaterials.create("com.example", "custom-bom", "1.0.0");
		InitializrMetadata metadata = InitializrMetadataTestBuilder.withDefaults().addBom("custom-bom", bom).build();
		String version = new SpringCloudProjectVersionResolver(metadata, this.versionResolver)
				.resolveVersion(VersionParser.DEFAULT.parse("2.1.0.RELEASE"), "com.example:test");
		assertThat(version).isNull();
	}

	@Test
	void resolveWithUnknownArtifactId() {
		BillOfMaterials bom = BillOfMaterials.create("org.springframework.cloud", "spring-cloud-dependencies", "1.0.0");
		InitializrMetadata metadata = InitializrMetadataTestBuilder.withDefaults().addBom("spring-cloud", bom).build();
		given(this.versionResolver.resolve("org.springframework.cloud", "spring-cloud-dependencies", "1.0.0"))
				.willReturn(Collections.singletonMap("org.springframework.cloud:spring-cloud", "1.1.0"));
		String version = new SpringCloudProjectVersionResolver(metadata, this.versionResolver)
				.resolveVersion(VersionParser.DEFAULT.parse("2.1.0.RELEASE"), "org.springframework.cloud:test");
		assertThat(version).isNull();
	}

	@Test
	void resolveManagedArtifact() {
		BillOfMaterials bom = BillOfMaterials.create("org.springframework.cloud", "spring-cloud-dependencies", "1.0.0");
		InitializrMetadata metadata = InitializrMetadataTestBuilder.withDefaults().addBom("spring-cloud", bom).build();
		given(this.versionResolver.resolve("org.springframework.cloud", "spring-cloud-dependencies", "1.0.0"))
				.willReturn(Collections.singletonMap("org.springframework.cloud:spring-cloud", "1.1.0"));
		String version = new SpringCloudProjectVersionResolver(metadata, this.versionResolver)
				.resolveVersion(VersionParser.DEFAULT.parse("2.1.0.RELEASE"), "org.springframework.cloud:spring-cloud");
		assertThat(version).isEqualTo("1.1.0");
	}

}
