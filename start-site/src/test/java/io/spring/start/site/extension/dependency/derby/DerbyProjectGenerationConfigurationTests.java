/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.derby;

import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DerbyProjectGenerationConfiguration}.
 */
class DerbyProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void whenDerbyIsSelectedAddsDerbytools() {
		assertThat(mavenPom(createProjectRequest("derby"))).hasDependency("org.apache.derby", "derbytools", null,
				"runtime");
	}

	@Test
	void whenDerbyIsNotSelectedDoesNotAddDerbytools() {
		assertThat(mavenPom(createProjectRequest("web"))).doesNotHaveDependency("org.apache.derby", "derbytools");
	}

}
