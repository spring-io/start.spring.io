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

package io.spring.start.site.extension.dependency.springboot;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringBootProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class SpringBootProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Nested
	class MavenTests {

		@Test
		void devtoolsIsOptional() {
			ProjectRequest request = createProjectRequest("devtools");
			assertThat(mavenPom(request))
				.hasText("/project/dependencies/dependency[2]/artifactId", "spring-boot-devtools")
				.hasText("/project/dependencies/dependency[2]/optional", "true");
		}

		@Test
		void devtoolsAbsentDoesNotChangeOptional() {
			ProjectRequest request = createProjectRequest("web");
			assertThat(mavenPom(request)).doesNotContain("optional");
		}

		@Test
		void dockerComposeSupportHasOptionalScope() {
			ProjectRequest request = createProjectRequest("docker-compose");
			assertThat(mavenPom(request))
				.hasText("/project/dependencies/dependency[2]/artifactId", "spring-boot-docker-compose")
				.hasText("/project/dependencies/dependency[2]/scope", "runtime")
				.hasText("/project/dependencies/dependency[2]/optional", "true");
		}

		@Test
		void springBoot31ConfiguresJammyBaseBuilder() {
			ProjectRequest request = createProjectRequest();
			request.setBootVersion("3.1.0");
			assertThat(mavenPom(request)).hasText("/project/build/plugins/plugin[1]/configuration/image/builder",
					PaketoBuilderBuildCustomizer.BASE_IMAGE_BUILDER);
		}

		@Test
		void springBoot31WithNativeConfiguresJammyTinyBuilder() {
			ProjectRequest request = createProjectRequest("native");
			request.setBootVersion("3.1.0");
			assertThat(mavenPom(request)).hasText("/project/build/plugins/plugin[2]/configuration/image/builder",
					PaketoBuilderBuildCustomizer.TINY_IMAGE_BUILDER);
		}

		@Test
		void springBoot32DoesNotConfigureJammyBaseBuilder() {
			ProjectRequest request = createProjectRequest();
			request.setBootVersion("3.2.0-RC1");
			assertThat(mavenPom(request)).doesNotContain("<builder>");
		}

	}

	@Nested
	class GradleTests {

		@Test
		void gradleWithDevtoolsConfigureBuild() {
			ProjectRequest request = createProjectRequest("devtools");
			assertThat(gradleBuild(request)).lines()
				.doesNotContain("configurations {")
				.contains("\tdevelopmentOnly 'org.springframework.boot:spring-boot-devtools'");
		}

		@Test
		void gradleWithoutDevtoolsDoesNotCreateDevelopmentOnlyConfiguration() {
			ProjectRequest request = createProjectRequest("web");
			assertThat(gradleBuild(request)).doesNotContain("developmentOnly");
		}

		@Test
		void gradleWithDockerComposeSupportUsesDevelopmentOnly() {
			ProjectRequest request = createProjectRequest("docker-compose");
			assertThat(gradleBuild(request)).lines()
				.contains("\tdevelopmentOnly 'org.springframework.boot:spring-boot-docker-compose'");
		}

		@Test
		void mavenWithDockerComposeSupportHasOptionalScope() {
			ProjectRequest request = createProjectRequest("docker-compose");
			assertThat(mavenPom(request))
				.hasText("/project/dependencies/dependency[2]/artifactId", "spring-boot-docker-compose")
				.hasText("/project/dependencies/dependency[2]/scope", "runtime")
				.hasText("/project/dependencies/dependency[2]/optional", "true");
		}

		@Test
		void springBoot31WithGroovyDslConfiguresJammyBaseBuilder() {
			ProjectRequest request = createProjectRequest();
			request.setBootVersion("3.1.0");
			assertThat(gradleBuild(request))
				.contains("builder = '%s'".formatted(PaketoBuilderBuildCustomizer.BASE_IMAGE_BUILDER));
		}

		@Test
		void springBoot31WithGroovyDslAndNativeConfiguresJammyTinyBuilder() {
			ProjectRequest request = createProjectRequest("native");
			request.setBootVersion("3.1.0");
			assertThat(gradleBuild(request))
				.contains("builder = '%s'".formatted(PaketoBuilderBuildCustomizer.TINY_IMAGE_BUILDER));
		}

		@Test
		void springBoot32WithGroovyDslDoesNotConfigureJammyBaseBuilder() {
			ProjectRequest request = createProjectRequest();
			request.setBootVersion("3.2.0-RC1");
			assertThat(gradleBuild(request)).doesNotContain("builder = '");
		}

		@Test
		void springBoot31WithKotlinDslConfiguresJammyBaseBuilder() {
			ProjectRequest request = createProjectRequest();
			request.setBootVersion("3.1.0");
			assertThat(gradleKotlinDslBuild(request))
				.contains("builder.set(\"%s\")".formatted(PaketoBuilderBuildCustomizer.BASE_IMAGE_BUILDER));
		}

		@Test
		void springBoot31WithKotlinDslAndNativeConfiguresJammyTinyBuilder() {
			ProjectRequest request = createProjectRequest("native");
			request.setBootVersion("3.1.0");
			assertThat(gradleKotlinDslBuild(request))
				.contains("builder.set(\"%s\")".formatted(PaketoBuilderBuildCustomizer.TINY_IMAGE_BUILDER));
		}

		@Test
		void springBoot32WithKotlinDslDoesNotConfigureJammyBaseBuilder() {
			ProjectRequest request = createProjectRequest();
			request.setBootVersion("3.2.0-RC1");
			assertThat(gradleKotlinDslBuild(request)).doesNotContain("builder.set(\"");
		}

	}

}
