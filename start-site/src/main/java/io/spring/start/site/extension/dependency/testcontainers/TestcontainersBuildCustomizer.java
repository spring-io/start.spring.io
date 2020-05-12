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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	 * Supported Testcontainers modules with a mapping to Initializr dependency id.
	 */
	private final List<TestcontainersModule> supportedModules = Arrays.asList(
			TestcontainersModule.rdbms("postgresql", "postgresql"), TestcontainersModule.rdbms("mysql", "mysql"),
			TestcontainersModule.rdbms("mssqlserver", "sqlserver"), TestcontainersModule.rdbms("oracle-xe", "oracle"),
			TestcontainersModule.noSql("neo4j", "data-neo4j"),
			TestcontainersModule.noSql("cassandra", "data-cassandra"),
			TestcontainersModule.noSql("cassandra", "data-cassandra-reactive"),
			TestcontainersModule.noSql("couchbase", "data-couchbase"),
			TestcontainersModule.noSql("couchbase", "data-couchbase-reactive"));

	private final InitializrMetadata metadata;

	public TestcontainersBuildCustomizer(InitializrMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public void customize(Build build) {
		DependencyContainer dependencies = build.dependencies();

		Map<Type, Long> addedDependencies = this.supportedModules.stream()
				.filter((module) -> dependencies.has(module.getInitializrDependencyName()))
				.peek((module) -> addTestcontainersDriverDependency(dependencies, module.getName()))
				.collect(Collectors.groupingBy(TestcontainersModule::getType, Collectors.counting()));

		if (addedDependencies.containsKey(Type.RDBMS) && addedDependencies.get(Type.RDBMS) > 0) {
			if (dependencies.has("data-r2dbc")) {
				addTestcontainersDriverDependency(dependencies, "r2dbc");
			}
		}

		if (!addedDependencies.isEmpty()) {
			this.removeTestcontainers(build);
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

	private static final class TestcontainersModule {

		private final String name;

		private final String initializrDependencyName;

		private final Type type;

		static TestcontainersModule rdbms(String name, String initializrDependencyName) {
			return new TestcontainersModule(name, initializrDependencyName, Type.RDBMS);
		}

		static TestcontainersModule noSql(String name, String initializrDependencyName) {
			return new TestcontainersModule(name, initializrDependencyName, Type.NO_SQL);
		}

		private TestcontainersModule(String name, String initializrDependencyName, Type type) {
			this.name = name;
			this.initializrDependencyName = initializrDependencyName;
			this.type = type;
		}

		String getName() {
			return this.name;
		}

		String getInitializrDependencyName() {
			return this.initializrDependencyName;
		}

		Type getType() {
			return this.type;
		}

	}

	private enum Type {

		RDBMS, NO_SQL;

	}

}
