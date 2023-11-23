/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.springazure;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.start.site.support.implicit.ImplicitDependency;
import io.spring.start.site.support.implicit.ImplicitDependency.Builder;

/**
 * A registry of available Spring Azure modules.
 *
 * @author Andy Wilkinson
 * @author Yonghui Ye
 * @author Stephane Nicoll
 */
abstract class SpringAzureModuleRegistry {

	static Iterable<ImplicitDependency> createSpringBootRegistry() {
		return create(
				onDependencies("actuator").customizeBuild(addDependency("spring-cloud-azure-starter-actuator"))
					.customizeHelpDocument(addReferenceLink("actuator", "Azure Actuator")),
				onDependencies("integration", "azure-storage")
					.customizeBuild(addDependency("spring-cloud-azure-starter-integration-storage-queue"))
					.customizeHelpDocument(
							addReferenceLink("spring-integration/storage-queue", "Azure Integration Storage Queue")),
				onDependencies("mysql", "azure-support")
					.customizeBuild(addDependency("spring-cloud-azure-starter-jdbc-mysql"))
					.customizeHelpDocument((helpDocument) -> helpDocument.gettingStarted()
						.addReferenceDocLink("https://aka.ms/spring/msdocs/mysql", "Azure MySQL support")),
				onDependencies("postgresql", "azure-support")
					.customizeBuild(addDependency("spring-cloud-azure-starter-jdbc-postgresql"))
					.customizeHelpDocument((helpDocument) -> helpDocument.gettingStarted()
						.addReferenceDocLink("https://aka.ms/spring/msdocs/postgresql", "Azure PostgreSQL support")));
	}

	private static Iterable<ImplicitDependency> create(ImplicitDependency.Builder... dependencies) {
		return Arrays.stream(dependencies).map(Builder::build).collect(Collectors.toList());
	}

	private static ImplicitDependency.Builder onDependencies(String... dependencyIds) {
		return new Builder()
			.match((build) -> build.dependencies()
				.items()
				.anyMatch((dependency) -> dependency.getGroupId().equals("com.azure.spring")))
			.matchAllDependencyIds(dependencyIds);
	}

	private static Consumer<Build> addDependency(String id) {
		return addDependency(id, DependencyScope.COMPILE);
	}

	private static Consumer<Build> addDependency(String id, DependencyScope scope) {
		return (build) -> build.dependencies().add(id, Dependency.withCoordinates("com.azure.spring", id).scope(scope));
	}

	private static Consumer<HelpDocument> addReferenceLink(String id, String description) {
		return (helpDocument) -> {
			String href = String.format("https://aka.ms/spring/docs/%s", id);
			helpDocument.gettingStarted().addReferenceDocLink(href, description);
		};
	}

}
