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

package io.spring.start.site.kotlin;

import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.spring.code.kotlin.KotlinVersionResolver;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

/**
 * {@link KotlinVersionResolver} that determines the Kotlin version using the dependency
 * management from the project description's Boot version.
 *
 * @author Andy Wilkinson
 */
public class ManagedDependenciesKotlinVersionResolver implements KotlinVersionResolver {

	private final DependencyManagementVersionResolver resolver;

	public ManagedDependenciesKotlinVersionResolver(DependencyManagementVersionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public String resolveKotlinVersion(ResolvedProjectDescription description) {
		String version = description.getPlatformVersion().toString();
		return this.resolver.resolve("org.springframework.boot", "spring-boot-dependencies", version)
				.get("org.jetbrains.kotlin:kotlin-reflect");
	}

}
