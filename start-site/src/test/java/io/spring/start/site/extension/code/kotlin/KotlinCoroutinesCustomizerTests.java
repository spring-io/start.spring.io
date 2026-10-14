/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.code.kotlin;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link KotlinCoroutinesCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class KotlinCoroutinesCustomizerTests extends AbstractExtensionTests {

	@Test
	void kotlinCoroutinesIsAdded() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setLanguage("kotlin");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild()
			.hasDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor")
			.hasDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-test", null, "test");
		assertThat(helpDocument(request)).contains(
				"* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/7.0.8/languages/kotlin/coroutines.html)");
	}

	@Test
	void kotlinCoroutinesIsNotAddedWithNonKotlinApp() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setLanguage("java");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor")
			.doesNotHaveDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-test");
	}

	@Test
	void kotlinCoroutinesIsNotAddedWithoutReactiveFacet() {
		ProjectRequest request = createProjectRequest("web");
		request.setLanguage("kotlin");
		assertThat(mavenPom(request)).doesNotHaveDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor")
			.doesNotHaveDependency("org.jetbrains.kotlinx", "kotlinx-coroutines-test");
	}

}
