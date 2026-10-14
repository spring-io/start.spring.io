/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.code.kotlin;

import java.util.Map;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;

/**
 * A project customizer for Kotlin Coroutines.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 */
public class KotlinCoroutinesCustomizer {

	private final BuildMetadataResolver buildResolver;

	private final ProjectDescription description;

	private final MavenVersionResolver versionResolver;

	public KotlinCoroutinesCustomizer(InitializrMetadata metadata, ProjectDescription description,
			MavenVersionResolver versionResolver) {
		this.buildResolver = new BuildMetadataResolver(metadata, description.getPlatformVersion());
		this.description = description;
		this.versionResolver = versionResolver;
	}

	public void customize(Build build) {
		if (hasReactiveFacet(build)) {
			build.dependencies()
				.add("kotlinx-coroutines-reactor",
						Dependency.withCoordinates("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor"));
			build.dependencies()
				.add("kotlinx-coroutines-test",
						Dependency.withCoordinates("org.jetbrains.kotlinx", "kotlinx-coroutines-test")
							.scope(DependencyScope.TEST_COMPILE));
		}
	}

	public void customize(HelpDocument document, Build build) {
		if (hasReactiveFacet(build)) {
			Map<String, String> resolve = this.versionResolver.resolveDependencies("org.springframework.boot",
					"spring-boot-dependencies", this.description.getPlatformVersion().toString());
			String frameworkVersion = resolve.get("org.springframework:spring-core");
			String versionToUse = (frameworkVersion != null) ? frameworkVersion : "current";
			String href = String.format(
					"https://docs.spring.io/spring-framework/reference/%s/languages/kotlin/coroutines.html",
					versionToUse);
			document.gettingStarted()
				.addReferenceDocLink(href, "Coroutines section of the Spring Framework Documentation");
		}
	}

	private boolean hasReactiveFacet(Build build) {
		return this.buildResolver.hasFacet(build, "reactive");
	}

}
