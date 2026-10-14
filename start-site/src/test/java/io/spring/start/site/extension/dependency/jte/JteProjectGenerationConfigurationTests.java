/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.jte;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link JteProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class JteProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V4_0;

	private final Dependency jte = Dependency.withId("jte", "gg.jte", "jte-spring-boot-starter-4");

	@Test
	void shouldDoNothingIfJteIsNotSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "web");
		assertThat(mavenPom(request)).doesNotHaveDependency(this.jte.getGroupId(), this.jte.getArtifactId());
		assertThat(mavenPom(request)).doesNotHaveDependency("gg.jte", "jte");
		ProjectStructure structure = generateProject(request);
		assertThat(structure).doesNotContainDirectories("src/main/jte");
		assertThat(applicationProperties(request)).lines().doesNotContain("gg.jte.development-mode=true");
		assertThat(gitIgnore(request)).lines().doesNotContain("/jte-classes/");
		assertThat(helpDocument(request)).lines().doesNotContain("## jte");
	}

	@Test
	void shouldCreateTemplateDirectory() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jte");
		ProjectStructure structure = generateProject(request);
		assertThat(structure).containsDirectories("src/main/jte");
	}

	@Test
	void shouldAddProperties() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jte");
		assertThat(applicationProperties(request)).lines().contains("gg.jte.development-mode=true");
	}

	@Test
	void shouldAddGitIgnoreEntries() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jte");
		assertThat(gitIgnore(request)).lines().contains("/jte-classes/");
	}

	@Test
	void shouldAddHelpSection() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jte");
		assertThat(helpDocument(request)).lines().contains("## jte");
	}

	@Test
	void shouldAddGradlePluginAndConfigureIt() {
		String jteVersion = getDependency("jte").getVersion();
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jte");
		assertThat(gradleBuild(request)).hasPlugin("gg.jte.gradle", jteVersion).containsIgnoringWhitespaces("""
				jte {
					generate()
					binaryStaticContent = true
				}
				""");
	}

	@Test
	void shouldAddMavenPluginAndConfigureIt() {
		String jteVersion = getDependency("jte").getVersion();
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "jte");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<plugin>
					<groupId>gg.jte</groupId>
					<artifactId>jte-maven-plugin</artifactId>
					<version>%s</version>
					<executions>
						<execution>
							<id>jte-generate</id>
							<phase>generate-sources</phase>
							<goals>
								<goal>generate</goal>
							</goals>
							<configuration>
								<sourceDirectory>${project.basedir}/src/main/jte</sourceDirectory>
								<contentType>Html</contentType>
								<binaryStaticContent>true</binaryStaticContent>
				    			<targetResourceDirectory>${project.build.outputDirectory}</targetResourceDirectory>
							</configuration>
						</execution>
					</executions>
				</plugin>
				""".formatted(jteVersion));
	}

}
