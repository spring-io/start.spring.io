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

package io.spring.start.site.extension.dependency.graalvm;

import java.nio.file.Path;

import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GraalVmProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
class GraalVmProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void gradleBuildWithoutNativeDoesNotConfigureNativeBuildTools() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(gradleBuild(request)).doesNotContain("org.graalvm.buildtools.native");
	}

	@Test
	void mavenBuildWithoutNativeDoesNotConfigureNativeBuildTools() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotContain("native-maven-plugin");
	}

	@Test
	void mavenBuildConfigureNativeBuildtoolsPlugint() {
		ProjectRequest request = createNativeProjectRequest();
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.graalvm.buildtools</groupId>",
				"				<artifactId>native-maven-plugin</artifactId>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void gradleBuildConfigureNativeBuildToolsPlugin(@TempDir Path temp) {
		String nbtVersion = NativeBuildtoolsVersionResolver.resolve(MavenVersionResolver.withCacheLocation(temp),
				Version.parse("3.1.0"));
		ProjectRequest request = createNativeProjectRequest();
		request.setBootVersion("3.1.0");
		assertThat(gradleBuild(request)).hasPlugin("org.graalvm.buildtools.native", nbtVersion);
	}

	@Test
	void gradleBuildWithoutJpaDoesNotConfigureHibernateEnhancePlugin() {
		assertThat(gradleBuild(createNativeProjectRequest())).doesNotContain("org.hibernate.orm");
	}

	@Test
	void gradleBuildAndGroovyDslWithJpaConfiguresHibernateEnhancePlugin() {
		ProjectRequest request = createNativeProjectRequest("data-jpa");
		assertThat(gradleBuild(request)).hasPlugin("org.hibernate.orm").lines().containsSequence(
		// @formatter:off
				"hibernate {",
				"	enhancement {",
				"		enableAssociationManagement = true",
				"	}",
				"}");
		// @formatter:on
	}

	@Test
	void gradleBuildAndKotlinDslWithJpaConfiguresHibernateEnhancePlugin() {
		ProjectRequest request = createNativeProjectRequest("data-jpa");
		assertThat(gradleKotlinDslBuild(request)).hasPlugin("org.hibernate.orm").lines().containsSequence(
		// @formatter:off
						"hibernate {",
						"	enhancement {",
						"		enableAssociationManagement.set(true)",
						"	}",
						"}");
		// @formatter:on
	}

	@Test
	void mavenBuildWithoutJpaDoesNotConfigureHibernateEnhancePlugin() {
		assertThat(mavenPom(createNativeProjectRequest())).doesNotContain("hibernate-enhance-maven-plugin");
	}

	@Test
	void mavenBuildWithJpaConfigureHibernateEnhancePlugin() {
		assertThat(mavenPom(createNativeProjectRequest("data-jpa"))).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.hibernate.orm.tooling</groupId>",
				"				<artifactId>hibernate-enhance-maven-plugin</artifactId>",
				"				<version>${hibernate.version}</version>",
				"				<executions>",
				"					<execution>",
				"						<id>enhance</id>",
				"						<goals>",
				"							<goal>enhance</goal>",
				"						</goals>",
				"						<configuration>",
				"							<enableLazyInitialization>true</enableLazyInitialization>",
				"							<enableDirtyTracking>true</enableDirtyTracking>",
				"							<enableAssociationManagement>true</enableAssociationManagement>",
				"						</configuration>",
				"					</execution>",
				"				</executions>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void groovyProjectDoesNotConfigureGraalVm() {
		ProjectRequest request = createNativeProjectRequest("data-jpa");
		request.setLanguage(GroovyLanguage.ID);
		assertThat(gradleBuild(request)).doesNotContain("graalvm").doesNotContain("org.hibernate.orm");
	}

	private ProjectRequest createNativeProjectRequest(String... dependencies) {
		ProjectRequest projectRequest = createProjectRequest(dependencies);
		projectRequest.getDependencies().add(0, "native");
		projectRequest.setBootVersion("3.1.0");
		return projectRequest;
	}

}
