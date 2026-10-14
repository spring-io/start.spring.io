/*
 * Copyright 2012 - present the original author or authors.
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
		SupportedBootVersion springBootVersion = SupportedBootVersion.latest();
		String[] dependencies = allDependencies(metadata, springBootVersion);
		ProjectRequest request = createProjectRequest(springBootVersion, dependencies);
		request.setType(type);
		assertThatCode(() -> generateProject(request)).doesNotThrowAnyException();
	}

	private static String[] allDependencies(InitializrMetadata metadata, SupportedBootVersion springBootVersion) {
		Version targetVersion = Version.parse(springBootVersion.getVersion());
		return metadata.getDependencies()
			.getAll()
			.stream()
			.filter((candidate) -> candidate.match(targetVersion))
			.map(Dependency::getId)
			.toArray(String[]::new);
	}

}
