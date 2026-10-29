/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.stubborn;

import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link StubbornContractProjectGenerationConfiguration}.
 *
 * @author Moritz Halbritter
 */
class StubbornContractProjectGenerationConfigurationTests extends AbstractExtensionTests {

	private static final String VERIFIER = "stubborn-contract-verifier";

	private static final String STUB_RUNNER = "stubborn-contract-stub-runner";

	private static final String MAVEN_CONTRACTS_DIRECTORY = "src/test/resources/contracts";

	private static final String GRADLE_CONTRACTS_DIRECTORY = "src/contractTest/resources/contracts";

	@Test
	void mavenContractsDirectoryIsCreated() {
		ProjectRequest request = createProjectRequest("web", VERIFIER);
		request.setType("maven-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve(MAVEN_CONTRACTS_DIRECTORY)).isDirectory();
	}

	@Test
	void mavenContractsDirectoryIsNotCreatedWithoutVerifier() {
		ProjectRequest request = createProjectRequest("web", STUB_RUNNER);
		request.setType("maven-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve(MAVEN_CONTRACTS_DIRECTORY)).doesNotExist();
	}

	@Test
	void gradleContractsDirectoryIsCreated() {
		ProjectRequest request = createProjectRequest("web", VERIFIER);
		request.setType("gradle-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve(GRADLE_CONTRACTS_DIRECTORY)).isDirectory();
	}

	@Test
	void gradleContractsDirectoryIsNotCreatedWithoutVerifier() {
		ProjectRequest request = createProjectRequest("web", STUB_RUNNER);
		request.setType("gradle-project");
		ProjectStructure structure = generateProject(request);
		assertThat(structure.getProjectDirectory().resolve(GRADLE_CONTRACTS_DIRECTORY)).doesNotExist();
	}

	@Test
	void mavenPluginIsAdded() {
		ProjectRequest request = createProjectRequest("web", VERIFIER);
		assertThat(mavenPom(request)).hasDependency(getDependency(VERIFIER))
			.hasText("/project/build/plugins/plugin[1]/groupId", "sh.stubborn")
			.hasText("/project/build/plugins/plugin[1]/artifactId", "stubborn-contract-maven-plugin")
			.hasText("/project/build/plugins/plugin[1]/version", "${stubborn-contract.version}")
			.hasText("/project/build/plugins/plugin[1]/extensions", "true")
			.doesNotContain("<testMode>");
	}

	@Test
	void mavenPluginIsNotAddedWithoutVerifier() {
		ProjectRequest request = createProjectRequest("web", STUB_RUNNER);
		assertThat(mavenPom(request)).doesNotContain("stubborn-contract-maven-plugin");
	}

	@Test
	void mavenUsesWebTestClientWithWebFlux() {
		ProjectRequest request = createProjectRequest("webflux", VERIFIER);
		Dependency webTestClient = Dependency.withId("rest-assured-spring-web-test-client", "io.rest-assured",
				"spring-web-test-client");
		webTestClient.setScope(Dependency.SCOPE_TEST);
		assertThat(mavenPom(request))
			.hasText("/project/build/plugins/plugin[1]/artifactId", "stubborn-contract-maven-plugin")
			.hasText("/project/build/plugins/plugin[1]/configuration/testMode", "WEBTESTCLIENT")
			.hasDependency(webTestClient);
	}

	@Test
	void mavenWebTestClientIsNotAddedWithoutWebFlux() {
		ProjectRequest request = createProjectRequest("web", VERIFIER);
		assertThat(mavenPom(request)).doesNotContain("spring-web-test-client");
	}

	@Test
	void gradlePluginIsAdded() {
		ProjectRequest request = createProjectRequest("web", VERIFIER);
		assertThat(gradleBuild(request)).hasPlugin("sh.stubborn.contract", stubbornVersion())
			.doesNotContain("contracts {")
			.doesNotContain("contractTest");
	}

	@Test
	void gradlePluginIsNotAddedWithoutVerifier() {
		ProjectRequest request = createProjectRequest("web", STUB_RUNNER);
		assertThat(gradleBuild(request)).doesNotContain("sh.stubborn.contract");
	}

	@Test
	void gradleGroovyDslUsesWebTestClientWithWebFlux() {
		ProjectRequest request = createProjectRequest("webflux", VERIFIER);
		assertThat(gradleBuild(request)).containsSubsequence("contracts {", "testMode = 'WebTestClient'");
	}

	@Test
	void gradleKotlinDslUsesWebTestClientWithWebFlux() {
		ProjectRequest request = createProjectRequest("webflux", VERIFIER);
		assertThat(gradleKotlinDslBuild(request)).hasPlugin("sh.stubborn.contract", stubbornVersion())
			.contains("import sh.stubborn.contract.verifier.config.TestMode")
			.containsSubsequence("contracts {", "testMode = TestMode.WEBTESTCLIENT");
	}

	@Test
	void gradleWebTestClientIsAddedWithWebFlux() {
		ProjectRequest request = createProjectRequest("webflux", VERIFIER);
		assertThat(gradleBuild(request)).contains("testImplementation 'io.rest-assured:spring-web-test-client'");
	}

	private String stubbornVersion() {
		return getBom("stubborn-contract", SupportedBootVersion.latest().getVersion()).getVersion();
	}

}
