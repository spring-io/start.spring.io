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

package io.spring.start.site.extension.code.kotlin;

import java.util.function.Function;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.code.kotlin.KotlinVersionResolver;
import io.spring.initializr.versionresolver.MavenVersionResolver;

/**
 * {@link KotlinVersionResolver} that determines the Kotlin version using the dependency
 * management from the project description's platform version.
 *
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class ManagedDependenciesKotlinVersionResolver implements KotlinVersionResolver {

	private final MavenVersionResolver versionResolver;

	private final Function<ProjectDescription, String> fallback;

	public ManagedDependenciesKotlinVersionResolver(MavenVersionResolver versionResolver,
			Function<ProjectDescription, String> fallback) {
		this.versionResolver = versionResolver;
		this.fallback = fallback;
	}

	@Override
	public String resolveKotlinVersion(ProjectDescription description) {
		String kotlinVersion = this.versionResolver
			.resolveDependencies("org.springframework.boot", "spring-boot-dependencies",
					description.getPlatformVersion().toString())
			.get("org.jetbrains.kotlin:kotlin-reflect");
		return (kotlinVersion != null) ? kotlinVersion : this.fallback.apply(description);
	}

}
