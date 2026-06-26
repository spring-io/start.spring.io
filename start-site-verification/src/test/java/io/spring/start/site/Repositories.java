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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.Version.Qualifier;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.Repository;
import org.eclipse.aether.repository.RemoteRepository;

class Repositories {

	private final InitializrMetadata metadata;

	private final Version springBootVersion;

	private final Set<String> bootRepositories;

	Repositories(InitializrMetadata metadata, Version springBootVersion) {
		this.metadata = metadata;
		this.springBootVersion = springBootVersion;
		this.bootRepositories = getBootRepositories();
	}

	List<RemoteRepository> getRepositories(Dependency dependency, List<BillOfMaterials> boms) {
		Map<String, RemoteRepository> repositories = new HashMap<>();
		repositories.put("central", DependencyResolver.mavenCentral);
		addDependencyRepositories(dependency, repositories);
		addBomRepositories(boms, repositories);
		addBootRepositories(repositories);
		return List.copyOf(repositories.values());
	}

	private void addBootRepositories(Map<String, RemoteRepository> repositories) {
		for (String repository : this.bootRepositories) {
			repositories.computeIfAbsent(repository, this::repositoryForId);
		}
	}

	private void addBomRepositories(List<BillOfMaterials> boms, Map<String, RemoteRepository> repositories) {
		for (BillOfMaterials bom : boms) {
			for (String repository : bom.getRepositories()) {
				repositories.computeIfAbsent(repository, this::repositoryForId);
			}
		}
	}

	private void addDependencyRepositories(Dependency dependency, Map<String, RemoteRepository> repositories) {
		String dependencyRepository = dependency.getRepository();
		if (dependencyRepository != null) {
			repositories.computeIfAbsent(dependencyRepository, this::repositoryForId);
		}
	}

	private RemoteRepository repositoryForId(String id) {
		Repository repository = this.metadata.getConfiguration().getEnv().getRepositories().get(id);
		return DependencyResolver.createRemoteRepository(id, repository.getUrl().toExternalForm(),
				repository.isSnapshotsEnabled());
	}

	private Set<String> getBootRepositories() {
		if (getReleaseType() == ReleaseType.SNAPSHOT) {
			return Set.of("spring-snapshots");
		}
		return Collections.emptySet();
	}

	private ReleaseType getReleaseType() {
		Qualifier qualifier = this.springBootVersion.getQualifier();
		if (qualifier == null) {
			return ReleaseType.GA;
		}
		String id = qualifier.getId();
		if ("RELEASE".equals(id)) {
			return ReleaseType.GA;
		}
		if (id.contains("SNAPSHOT")) {
			return ReleaseType.SNAPSHOT;
		}
		return ReleaseType.MILESTONE;
	}

	private enum ReleaseType {

		GA, MILESTONE, SNAPSHOT

	}

}
