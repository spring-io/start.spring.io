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

package io.spring.start.site.extension.build.gradle;

import java.util.function.Function;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.gradle.DependencyManagementPluginVersionResolver;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

/**
 * {@link DependencyManagementPluginVersionResolver} that determines the dependency
 * management plugin version using the dependency management from the project
 * description's Boot version.
 *
 * @author Stephane Nicoll
 */
public class ManagedDependenciesDependencyManagementPluginVersionResolver
		implements DependencyManagementPluginVersionResolver {

	private final DependencyManagementVersionResolver resolver;

	private final Function<ProjectDescription, String> fallback;

	public ManagedDependenciesDependencyManagementPluginVersionResolver(DependencyManagementVersionResolver resolver,
			Function<ProjectDescription, String> fallback) {
		this.resolver = resolver;
		this.fallback = fallback;
	}

	@Override
	public String resolveDependencyManagementPluginVersion(ProjectDescription description) {
		String pluginVersion = this.resolver
				.resolve("org.springframework.boot", "spring-boot-dependencies",
						description.getPlatformVersion().toString())
				.get("io.spring.gradle:dependency-management-plugin");
		return (pluginVersion != null) ? pluginVersion : this.fallback.apply(description);
	}

}
