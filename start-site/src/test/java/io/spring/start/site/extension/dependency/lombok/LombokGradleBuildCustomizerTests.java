/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.lombok;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LombokGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class LombokGradleBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldConfigureLombokAsAnnotationProcessorsAndClasspathDependencies() {
		ProjectRequest request = createProjectRequest("lombok");
		assertThat(gradleBuild(request)).contains("annotationProcessor 'org.projectlombok:lombok'")
			.contains("compileOnly 'org.projectlombok:lombok'")
			.contains("testAnnotationProcessor 'org.projectlombok:lombok'")
			.contains("testCompileOnly 'org.projectlombok:lombok'");
	}

	@Test
	void lombokNotAddedIfLombokIsNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(gradleBuild(request)).doesNotContain("compileOnly 'org.projectlombok:lombok'");
	}

}
