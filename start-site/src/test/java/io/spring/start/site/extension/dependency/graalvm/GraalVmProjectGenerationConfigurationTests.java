/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link GraalVmProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
class GraalVmProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.latest();

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
	void mavenBuildConfigureNativeBuildtoolsPlugin() {
		ProjectRequest request = createNativeProjectRequest(BOOT_VERSION);
		assertThat(mavenPom(request)).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.graalvm.buildtools</groupId>",
				"				<artifactId>native-maven-plugin</artifactId>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void gradleBuildConfigureNativeBuildToolsPlugin(@Autowired MavenVersionResolver mavenVersionResolver) {
		String nbtVersion = NativeBuildtoolsVersionResolver.resolve(mavenVersionResolver,
				Version.parse(SupportedBootVersion.latest().getVersion()));
		ProjectRequest request = createNativeProjectRequest(BOOT_VERSION);
		assertThat(gradleBuild(request)).hasPlugin("org.graalvm.buildtools.native", nbtVersion);
	}

	@Test
	void gradleBuildWithoutJpaDoesNotConfigureHibernateEnhancePlugin() {
		assertThat(gradleBuild(createNativeProjectRequest(BOOT_VERSION))).doesNotContain("org.hibernate.orm");
	}

	@Test
	void gradleBuildAndGroovyDslWithJpaConfiguresHibernateEnhancePlugin() {
		ProjectRequest request = createNativeProjectRequest(BOOT_VERSION, "data-jpa");
		assertThat(gradleBuild(request)).hasPlugin("org.hibernate.orm").lines().containsSequence(
		// @formatter:off
				"hibernate {",
				"	enhancement {",
				"	}",
				"}");
		// @formatter:on
	}

	@Test
	void gradleBuildAndKotlinDslWithJpaConfiguresHibernateEnhancePlugin() {
		ProjectRequest request = createNativeProjectRequest(BOOT_VERSION, "data-jpa");
		assertThat(gradleKotlinDslBuild(request)).hasPlugin("org.hibernate.orm").lines().containsSequence(
		// @formatter:off
						"hibernate {",
						"	enhancement {",
						"	}",
						"}");
		// @formatter:on
	}

	@Test
	void mavenBuildWithoutJpaDoesNotConfigureHibernateEnhancePlugin() {
		assertThat(mavenPom(createNativeProjectRequest(BOOT_VERSION))).doesNotContain("hibernate-maven-plugin");
	}

	@Test
	void mavenBuildWithJpaConfigureHibernateEnhancePlugin() {
		assertThat(mavenPom(createNativeProjectRequest(BOOT_VERSION, "data-jpa"))).lines().containsSequence(
		// @formatter:off
				"			<plugin>",
				"				<groupId>org.hibernate.orm</groupId>",
				"				<artifactId>hibernate-maven-plugin</artifactId>",
				"				<version>${hibernate.version}</version>",
				"				<executions>",
				"					<execution>",
				"						<id>enhance</id>",
				"						<goals>",
				"							<goal>enhance</goal>",
				"						</goals>",
				"					</execution>",
				"				</executions>",
				"			</plugin>");
		// @formatter:on
	}

	@Test
	void groovyProjectDoesNotConfigureGraalVm() {
		ProjectRequest request = createNativeProjectRequest(BOOT_VERSION, "data-jpa");
		request.setLanguage(GroovyLanguage.ID);
		assertThat(gradleBuild(request)).doesNotContain("graalvm").doesNotContain("org.hibernate.orm");
	}

	private ProjectRequest createNativeProjectRequest(SupportedBootVersion bootVersion, String... dependencies) {
		ProjectRequest projectRequest = createProjectRequest(bootVersion, dependencies);
		projectRequest.getDependencies().add(0, "native");
		return projectRequest;
	}

}
