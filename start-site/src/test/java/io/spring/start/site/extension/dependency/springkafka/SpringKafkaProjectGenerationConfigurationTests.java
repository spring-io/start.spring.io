/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringKafkaProjectGenerationConfiguration}.
 *
 * @author Wonwoo Lee
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 * @author Venkata Naga Sai Srikanth Gollapudi
 */
class SpringKafkaProjectGenerationConfigurationTests extends AbstractExtensionTests {

	@Test
	void customizesBuildpacksBuilderWhenUsingMavenAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(mavenPom(request)).doesNotContain("runImage");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleGroovyAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(gradleBuild(request)).doesNotContain("runImage");
	}

	@Test
	void customizesBuildpacksBuilderWhenUsingGradleKotlinAndKafkaStreams() {
		ProjectRequest request = createProjectRequest("kafka-streams");
		assertThat(gradleKotlinDslBuild(request)).doesNotContain("runImage");
	}

}
