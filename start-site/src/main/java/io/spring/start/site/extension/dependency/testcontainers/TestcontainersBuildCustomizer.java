/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.HashMap;
import java.util.Map;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyContainer;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * {@link BuildCustomizer} for Testcontainers that adds Testcontainers database specific
 * modules if database drivers are added as dependency.
 *
 * @author Maciej Walkowiak
 */
public class TestcontainersBuildCustomizer implements BuildCustomizer<Build> {

	/**
	 * Map from Spring Boot dependency id to Testcontainers artifactId for each supported
	 * database.
	 */
	private final Map<String, String> driverToTestContainersDependency = new HashMap<>();

	private final InitializrMetadata metadata;

	public TestcontainersBuildCustomizer(InitializrMetadata metadata) {
		this.metadata = metadata;
		this.driverToTestContainersDependency.put("postgresql", "postgresql");
		this.driverToTestContainersDependency.put("mysql", "mysql");
		this.driverToTestContainersDependency.put("sqlserver", "mssqlserver");
		this.driverToTestContainersDependency.put("oracle", "oracle-xe");
	}

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();

		long includedDrivers = this.driverToTestContainersDependency.entrySet().stream()
				.filter((entry) -> dependencies.has(entry.getKey()))
				.peek((entry) -> addTestcontainersDriverDependency(dependencies, entry.getValue())).count();

		if (includedDrivers > 0) {
			removeTestcontainers(build);

			if (dependencies.has("data-r2dbc")) {
				addTestcontainersDriverDependency(dependencies, "r2dbc");
			}
		}
	}

	private void addTestcontainersDriverDependency(DependencyContainer dependencies, String testcontainersArtifactId) {
		dependencies.add("testcontainers-" + testcontainersArtifactId,
				Dependency.withCoordinates("org.testcontainers", testcontainersArtifactId)
						.scope(DependencyScope.TEST_COMPILE).build());
	}

	/**
	 * Removes Testcontainers core dependency which is not needed as it is a transient
	 * dependency for database specific Testcontainers modules.
	 * @param build - build configuration for the project
	 */
	private void removeTestcontainers(Build build) {
		io.spring.initializr.metadata.Dependency testcontainers = this.metadata.getDependencies().get("testcontainers");
		BillOfMaterials bom = this.metadata.getConfiguration().getEnv().getBoms().get(testcontainers.getBom());
		if (bom.getVersionProperty() != null) {
			build.properties().version(bom.getVersionProperty(), bom.getVersion());
		}
		build.boms().add(testcontainers.getBom());
		build.dependencies().remove("testcontainers");
	}

}
