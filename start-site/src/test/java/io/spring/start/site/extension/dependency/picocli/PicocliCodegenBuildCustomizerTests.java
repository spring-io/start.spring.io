/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.picocli;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link PicocliCodegenBuildCustomizer}.
 *
 * @author Brian Clozel
 */
class PicocliCodegenBuildCustomizerTests extends AbstractExtensionTests {

	private static final Version SPRING_BOOT_3_VERSION = Version.parse("3.1.0");

	@Test
	void picocliCodegenIsAddedAsMavenOptionalDependency() {
		ProjectRequest request = createProjectRequest("picocli", "native");
		request.setBootVersion(SPRING_BOOT_3_VERSION.toString());
		assertThat(mavenPom(request)).hasDependency(getPicocli(SPRING_BOOT_3_VERSION))
			.hasDependency(getPicocliCodegen(SPRING_BOOT_3_VERSION))
			.hasText("/project/dependencies/dependency[2]/optional", "true");
	}

	@Test
	void picocliCodegenIsNotAddedAsMavenDependencyIfNativeNotSelected() {
		ProjectRequest request = createProjectRequest("picocli");
		request.setBootVersion(SPRING_BOOT_3_VERSION.toString());
		assertThat(mavenPom(request)).hasDependency(getDependency("picocli"))
			.doesNotHaveDependency("info.picocli", "picocli-codegen");
	}

	@Test
	void picocliCodegenIsAddedAsGradleAnnotationProcessor() {
		ProjectRequest request = createProjectRequest("picocli", "native");
		request.setBootVersion(SPRING_BOOT_3_VERSION.toString());
		assertThat(gradleBuild(request)).contains("annotationProcessor 'info.picocli:picocli-codegen:"
				+ getPicocli(SPRING_BOOT_3_VERSION).getVersion() + "'");
	}

	@Test
	void picocliCodegenIsNotAddedAsGradleAnnotationProcessorIfNativeNotSelected() {
		ProjectRequest request = createProjectRequest("picocli");
		request.setBootVersion(SPRING_BOOT_3_VERSION.toString());
		assertThat(gradleBuild(request)).doesNotContain("info.picocli:picocli-codegen");
	}

	private Dependency getPicocli(Version springBootVersion) {
		return getDependency("picocli").resolve(springBootVersion);
	}

	private Dependency getPicocliCodegen(Version springBootVersion) {
		return Dependency.create("info.picocli", "picocli-codegen", getPicocli(springBootVersion).getVersion(),
				Dependency.SCOPE_COMPILE);
	}

}
