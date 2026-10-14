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
 * Tests for {@link ReactorKotlinExtensionsCustomizer}.
 *
 * @author Eddú Meléndez
 */
class ReactorKotlinExtensionTests extends AbstractExtensionTests {

	@Test
	void reactorKotlinExtensionsIsAdded() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setLanguage("kotlin");
		ProjectStructure project = generateProject(request);
		assertThat(project).mavenBuild().hasDependency("io.projectreactor.kotlin", "reactor-kotlin-extensions");
	}

	@Test
	void reactorKotlinExtensionsIsNotAddedWithNonKotlinApp() {
		ProjectRequest request = createProjectRequest("webflux");
		request.setLanguage("java");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.projectreactor.kotlin", "reactor-kotlin-extensions");
	}

	@Test
	void reactorKotlinExtensionsIsNotAddedWithoutReactiveFacet() {
		ProjectRequest request = createProjectRequest("web");
		request.setLanguage("kotlin");
		assertThat(mavenPom(request)).doesNotHaveDependency("io.projectreactor.kotlin", "reactor-kotlin-extensions");
	}

}
