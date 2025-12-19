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

package io.spring.start.site.extension;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests with combined extensions.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
public class ExtensionIntegrationTests extends AbstractExtensionTests {

	@ParameterizedTest
	@ValueSource(strings = { "maven-project", "gradle-project", "gradle-project-kotlin" })
	void projectWithAllDependenciesCanBeGenerated(String type) {
		InitializrMetadata metadata = getMetadata();
		String platformVersion = metadata.getBootVersions().getDefault().getId();
		String[] dependencies = allDependencies(metadata, platformVersion);
		ProjectRequest request = createProjectRequest(SupportedBootVersion.latest(), dependencies);
		request.setType(type);
		assertThatCode(() -> generateProject(request)).doesNotThrowAnyException();
	}

	private static String[] allDependencies(InitializrMetadata metadata, String platformVersion) {
		Version targetVersion = Version.parse(platformVersion);
		return metadata.getDependencies()
			.getAll()
			.stream()
			.filter((candidate) -> candidate.match(targetVersion))
			.map(Dependency::getId)
			.toArray(String[]::new);
	}

}
