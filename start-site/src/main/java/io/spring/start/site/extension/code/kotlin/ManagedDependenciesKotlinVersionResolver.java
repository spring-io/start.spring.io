/*
 * Copyright 2012 - present the original author or authors.
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
