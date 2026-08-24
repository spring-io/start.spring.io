/*
 * Copyright 2012 - present the original author or authors.
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

package io.spring.start.site.project;

import java.util.List;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Integration tests for {@link JavaVersionMapping} using the Spring Boot versions fetched
 * by {@link io.spring.start.site.support.StartInitializrMetadataUpdateStrategy} from
 * api.spring.io.
 *
 * @author Moritz Halbritter
 */
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
class JavaVersionMappingIntegrationTests {

	private final JavaVersionMapping mapping = new JavaVersionMapping();

	private final KotlinVersionMapping kotlinVersionMapping = new KotlinVersionMapping();

	private final InitializrMetadataProvider metadataProvider;

	JavaVersionMappingIntegrationTests(@Autowired InitializrMetadataProvider metadataProvider) {
		this.metadataProvider = metadataProvider;
	}

	@Test
	void doesNotLogWarningsForAvailableBootVersions(CapturedOutput output) {
		List<Version> bootVersions = this.metadataProvider.get()
			.getBootVersions()
			.getContent()
			.stream()
			.map((element) -> Version.parse(element.getId()))
			.toList();
		assertThat(bootVersions).isNotEmpty();
		for (Version bootVersion : bootVersions) {
			this.mapping.getMinJavaVersion(bootVersion);
			this.mapping.getMaxJavaVersion(bootVersion);
			Version kotlinVersion = this.mapping.getKotlinVersion(bootVersion);
			assertThatCode(() -> this.kotlinVersionMapping.getMinJavaVersion(kotlinVersion))
				.as("Kotlin version %s used by Spring Boot %s", kotlinVersion, bootVersion)
				.doesNotThrowAnyException();
		}
		assertThat(output).doesNotContain("Failed to find mapping for Spring Boot");
	}

}
