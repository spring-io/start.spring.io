/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.lombok;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LombokMavenBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class LombokMavenBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldConfigureLombokAsAnnotationProcessorsAndClasspathDependencies() {
		ProjectRequest request = createProjectRequest("lombok");
		assertThat(mavenPom(request)).containsIgnoringWhitespaces("""
				<dependency>
					<groupId>org.projectlombok</groupId>
					<artifactId>lombok</artifactId>
					<optional>true</optional>
				</dependency>
				""").containsIgnoringWhitespaces("""
				<execution>
					<id>default-compile</id>
					<phase>compile</phase>
					<goals>
						<goal>compile</goal>
					</goals>
					<configuration>
						<annotationProcessorPaths>
							<path>
								<groupId>org.projectlombok</groupId>
								<artifactId>lombok</artifactId>
							</path>
						</annotationProcessorPaths>
					</configuration>
				</execution>
				""").containsIgnoringWhitespaces("""
					<execution>
						<id>default-testCompile</id>
						<phase>test-compile</phase>
						<goals>
							<goal>testCompile</goal>
						</goals>
						<configuration>
							<annotationProcessorPaths>
								<path>
									<groupId>org.projectlombok</groupId>
									<artifactId>lombok</artifactId>
								</path>
							</annotationProcessorPaths>
						</configuration>
					</execution>
				</executions>
				""");
	}

	@Test
	void lombokNotAddedIfLombokIsNotSelected() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotContain("<artifactId>lombok</artifactId>");
	}

}
