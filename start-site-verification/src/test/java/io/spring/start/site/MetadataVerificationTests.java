/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.DependencyGroup;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.metadata.Repository;
import org.eclipse.aether.repository.RemoteRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests to verify the validity of the metadata.
 *
 * @author Andy Wilkinson
 */
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class MetadataVerificationTests {

	private final InitializrMetadata metadata;

	MetadataVerificationTests(@Autowired InitializrMetadataProvider metadataProvider) throws IOException {
		this.metadata = metadataProvider.get();
	}

	@AfterAll
	static void cleanUp() {
		DependencyResolver.cleanUp();
	}

	@ParameterizedTest(name = "{3}")
	@MethodSource("parameters")
	void dependencyStarterConfigurationIsCorrect(Dependency dependency, List<BillOfMaterials> boms,
			List<RemoteRepository> repositories, String description) {
		List<String> dependencies = collectDependencies(dependency, boms, repositories);
		if (dependencies != null) {
			if (dependency.isStarter()) {
				assertThat(dependencies).anyMatch("org.springframework.boot:spring-boot-starter"::equals);
			}
			else {
				assertThat(dependencies).noneMatch("org.springframework.boot:spring-boot-starter"::equals);
			}
		}
	}

	private List<String> collectDependencies(Dependency dependency, List<BillOfMaterials> boms,
			List<RemoteRepository> repositories) {
		try {
			return DependencyResolver.resolveDependencies(dependency.getGroupId(), dependency.getArtifactId(),
					dependency.getVersion(), boms, repositories);
		}
		catch (RuntimeException ex) {
			// Known issue with Spring Cloud Contract to be fixed in the next release
			// See
			// https://github.com/spring-cloud/spring-cloud-contract/commit/13c7d477fbbc856b319600874a11aabcef283df7
			if (ex.getMessage()
					.contains("org.springframework.cloud:spring-cloud-starter-contract-verifier:pom:2.2.3.RELEASE")) {
				return null;
			}
			throw ex;
		}
	}

	Stream<Arguments> parameters() {
		List<Arguments> parameters = new ArrayList<>();
		for (Version bootVersion : bootVersions()) {
			for (DependencyGroup group : groups()) {
				for (Dependency dependency : dependenciesForBootVersion(group, bootVersion)) {
					dependency = dependency.resolve(bootVersion);
					List<BillOfMaterials> boms = getBoms(dependency, bootVersion);
					List<RemoteRepository> repositories = getRepositories(dependency, bootVersion, boms);
					parameters
							.add(Arguments.of(dependency, boms, repositories, bootVersion + " " + dependency.getId()));
				}
			}
		}
		return parameters.stream();
	}

	private List<BillOfMaterials> getBoms(Dependency dependency, Version bootVersion) {
		List<BillOfMaterials> boms = new ArrayList<>();
		bomsForId(dependency.getBom(), bootVersion, boms::add);
		boms.add(
				BillOfMaterials.create("org.springframework.boot", "spring-boot-dependencies", bootVersion.toString()));
		return boms;
	}

	private void bomsForId(String id, Version bootVersion, Consumer<BillOfMaterials> consumer) {
		if (id == null) {
			return;
		}
		Map<String, BillOfMaterials> bomsById = this.metadata.getConfiguration().getEnv().getBoms();
		BillOfMaterials bom = bomsById.get(id);
		if (bom != null) {
			consumer.accept(bom.resolve(bootVersion));
			bom.getAdditionalBoms().forEach((additionalBomId) -> bomsForId(additionalBomId, bootVersion, consumer));
		}
	}

	private List<RemoteRepository> getRepositories(Dependency dependency, Version bootVersion,
			List<BillOfMaterials> boms) {
		Map<String, RemoteRepository> repositories = new HashMap<>();
		repositories.put("central", DependencyResolver.mavenCentral);
		String dependencyRepository = dependency.getRepository();
		if (dependencyRepository != null) {
			repositories.computeIfAbsent(dependencyRepository, this::repositoryForId);
		}
		for (BillOfMaterials bom : boms) {
			for (String repository : bom.getRepositories()) {
				repositories.computeIfAbsent(repository, this::repositoryForId);
			}
		}
		if (bootVersion.getQualifier() != null) {
			String qualifier = bootVersion.getQualifier().getId();
			if (!qualifier.equals("RELEASE")) {
				repositories.computeIfAbsent("spring-milestones", this::repositoryForId);
				if (qualifier.contains("SNAPSHOT")) {
					repositories.computeIfAbsent("spring-snapshots", this::repositoryForId);
				}
			}
		}
		return new ArrayList<>(repositories.values());
	}

	private RemoteRepository repositoryForId(String id) {
		Repository repository = this.metadata.getConfiguration().getEnv().getRepositories().get(id);
		return DependencyResolver.createRemoteRepository(id, repository.getUrl().toExternalForm(),
				repository.isSnapshotsEnabled());
	}

	private Collection<Version> bootVersions() {
		return this.metadata.getBootVersions().getContent().stream().map(DefaultMetadataElement::getId)
				.map(VersionParser.DEFAULT::parse).collect(Collectors.toList());
	}

	private Collection<DependencyGroup> groups() {
		return this.metadata.getDependencies().getContent();
	}

	private Collection<Dependency> dependenciesForBootVersion(DependencyGroup group, Version bootVersion) {
		return group.getContent().stream().filter((dependency) -> dependency.match(bootVersion))
				.collect(Collectors.toList());
	}

}
