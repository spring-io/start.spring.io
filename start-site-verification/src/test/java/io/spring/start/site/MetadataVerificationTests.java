/*
 * Copyright 2012 - present the original author or authors.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.DefaultMetadataElement;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.DependencyGroup;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.start.testsupport.Homes;
import org.eclipse.aether.repository.RemoteRepository;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests to verify the validity of the metadata.
 *
 * @author Andy Wilkinson
 * @author Moritz Halbritter
 */
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
@ActiveProfiles("test")
class MetadataVerificationTests {

	private static final Set<String> IGNORED_DEPENDENCIES = Set.of(
			"com.vmware.tanzu.spring.governance:governance-starter",
			"com.vmware.tanzu.springcloudgateway.extensions:access-control",
			"com.vmware.tanzu.springcloudgateway.extensions:custom",
			"com.vmware.tanzu.springcloudgateway.extensions:graphql",
			"com.vmware.tanzu.springcloudgateway.extensions:sso",
			"com.vmware.tanzu.springcloudgateway.extensions:traffic-control",
			"com.vmware.tanzu.springcloudgateway.extensions:transformation");

	private final InitializrMetadata metadata;

	MetadataVerificationTests(@Autowired InitializrMetadataProvider metadataProvider) {
		this.metadata = metadataProvider.get();
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
		return DependencyResolver.resolveDependencies(Homes.MAVEN, dependency.getGroupId(), dependency.getArtifactId(),
				dependency.getVersion(), boms, repositories);
	}

	Stream<Arguments> parameters() {
		List<Arguments> parameters = new ArrayList<>();
		for (Version bootVersion : bootVersions()) {
			Repositories repositories = new Repositories(this.metadata, bootVersion);
			for (DependencyGroup group : groups()) {
				for (Dependency dependency : dependenciesForBootVersion(group, bootVersion)) {
					dependency = dependency.resolve(bootVersion);
					List<BillOfMaterials> boms = getBoms(dependency, bootVersion);
					parameters.add(Arguments.of(dependency, boms, repositories.getRepositories(dependency, boms),
							bootVersion + " " + dependency.getId()));
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

	private Collection<Version> bootVersions() {
		return this.metadata.getBootVersions()
			.getContent()
			.stream()
			.map(DefaultMetadataElement::getId)
			.map(VersionParser.DEFAULT::parse)
			.toList();
	}

	private Collection<DependencyGroup> groups() {
		return this.metadata.getDependencies().getContent();
	}

	private Collection<Dependency> dependenciesForBootVersion(DependencyGroup group, Version bootVersion) {
		return group.getContent()
			.stream()
			.filter((dependency) -> !isIgnored(dependency))
			.filter((dependency) -> dependency.match(bootVersion))
			.toList();
	}

	private boolean isIgnored(Dependency dependency) {
		String coordinates = "%s:%s".formatted(dependency.getGroupId(), dependency.getArtifactId());
		return IGNORED_DEPENDENCIES.contains(coordinates);
	}

}
