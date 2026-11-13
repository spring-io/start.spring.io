/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.code.kotlin;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link KotlinProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class KotlinProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void mavenEmitsTypeAnnotationsWithValidation() {
		ProjectRequest request = kotlinRequest("validation");
		assertThat(mavenPom(request)).contains("<arg>-Xemit-jvm-type-annotations</arg>");
	}

	@Test
	void gradleEmitsTypeAnnotationsWithValidation() {
		ProjectRequest request = kotlinRequest("validation");
		assertThat(gradleBuild(request)).contains("'-Xemit-jvm-type-annotations'");
	}

	@Test
	void gradleKotlinDslEmitsTypeAnnotationsWithValidation() {
		ProjectRequest request = kotlinRequest("validation");
		assertThat(gradleKotlinDslBuild(request)).contains("\"-Xemit-jvm-type-annotations\"");
	}

	@Test
	void typeAnnotationsAreNotEmittedWithoutValidation() {
		ProjectRequest request = kotlinRequest("web");
		assertThat(mavenPom(request)).doesNotContain("-Xemit-jvm-type-annotations");
	}

	@Test
	void typeAnnotationsAreNotEmittedForJava() {
		ProjectRequest request = createProjectRequest("validation");
		request.setLanguage("java");
		assertThat(mavenPom(request)).doesNotContain("-Xemit-jvm-type-annotations");
	}

	private ProjectRequest kotlinRequest(String... dependencies) {
		ProjectRequest request = createProjectRequest(dependencies);
		request.setLanguage("kotlin");
		return request;
	}

}
