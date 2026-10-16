/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests checking that the compatibility ranges of BOMs and the dependencies
 * using them are aligned.
 *
 * @author Moritz Halbritter
 */
@SpringBootTest
@ActiveProfiles("test")
class BomRangeIntegrationTests {

	private final Map<String, BillOfMaterials> boms;

	private final Collection<Dependency> dependencies;

	BomRangeIntegrationTests(@Autowired InitializrMetadataProvider metadataProvider) {
		this.boms = metadataProvider.get().getConfiguration().getEnv().getBoms();
		this.dependencies = metadataProvider.get().getDependencies().getAll();
	}

	@Test
	void dependenciesCoverBomRange() {
		List<String> problems = new ArrayList<>();
		this.boms.forEach((id, bom) -> {
			List<Dependency> users = getUsers(id);
			if (bom.getMappings().isEmpty() || users.isEmpty()) {
				return;
			}
			for (Version version : getProbes(bom, users)) {
				if (!isCovered(bom, version)) {
					continue;
				}
				if (users.stream().noneMatch((dependency) -> usesBom(dependency, id, version))) {
					problems.add("BOM '%s' %s maps %s but no dependency uses it: %s".formatted(id, describe(bom),
							version, describe(users)));
				}
			}
		});
		assertThat(problems).isEmpty();
	}

	@Test
	void bomCoversDependencyRange() {
		List<String> problems = new ArrayList<>();
		this.boms.forEach((id, bom) -> {
			List<Dependency> users = getUsers(id);
			for (Version version : getProbes(bom, users)) {
				if (isCovered(bom, version)) {
					continue;
				}
				for (Dependency dependency : users) {
					if (usesBom(dependency, id, version)) {
						problems.add("Dependency %s uses BOM '%s' %s on %s but BOM has no mapping"
							.formatted(describe(dependency), id, describe(bom), version));
					}
				}
			}
		});
		assertThat(problems).isEmpty();
	}

	private String describe(BillOfMaterials bom) {
		return bom.getMappings()
			.stream()
			.map(BillOfMaterials.Mapping::getCompatibilityRange)
			.collect(Collectors.joining(", "));
	}

	private String describe(List<Dependency> dependencies) {
		return dependencies.stream().map(this::describe).collect(Collectors.joining(", "));
	}

	private String describe(Dependency dependency) {
		String range = (dependency.getCompatibilityRange() != null) ? dependency.getCompatibilityRange() : "(any)";
		return "'%s' %s".formatted(dependency.getId(), range);
	}

	private List<Dependency> getUsers(String bomId) {
		return this.dependencies.stream()
			.filter((dependency) -> bomId.equals(dependency.getBom())
					|| dependency.getMappings().stream().anyMatch((mapping) -> bomId.equals(mapping.getBom())))
			.toList();
	}

	private boolean usesBom(Dependency dependency, String bomId, Version version) {
		return dependency.match(version) && bomId.equals(dependency.resolve(version).getBom());
	}

	private boolean isCovered(BillOfMaterials bom, Version version) {
		if (bom.getMappings().isEmpty()) {
			return true;
		}
		return bom.getMappings().stream().anyMatch((mapping) -> matches(mapping.getRange(), version));
	}

	private boolean matches(VersionRange range, Version version) {
		return range != null && range.match(version);
	}

	private TreeSet<Version> getProbes(BillOfMaterials bom, List<Dependency> users) {
		Stream<VersionRange> bomRanges = bom.getMappings().stream().map(BillOfMaterials.Mapping::getRange);
		Stream<VersionRange> dependencyRanges = users.stream()
			.flatMap((dependency) -> Stream.concat(Stream.of(dependency.getRange()),
					dependency.getMappings().stream().map(Dependency.Mapping::getRange)));
		TreeSet<Version> probes = new TreeSet<>();
		Stream.concat(bomRanges, dependencyRanges)
			.filter(Objects::nonNull)
			.flatMap((range) -> Stream.of(range.getLowerVersion(), range.getHigherVersion()))
			.filter(Objects::nonNull)
			.forEach(probes::add);
		return probes;
	}

}
