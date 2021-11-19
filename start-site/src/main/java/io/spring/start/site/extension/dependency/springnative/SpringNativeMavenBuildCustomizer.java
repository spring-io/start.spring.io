/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.springnative;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.buildsystem.maven.MavenProfile;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionProperty;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.generator.version.VersionReference;

import org.springframework.core.Ordered;

/**
 * A {@link BuildCustomizer} that configures Spring Native for Maven.
 *
 * @author Stephane Nicoll
 */
class SpringNativeMavenBuildCustomizer implements BuildCustomizer<MavenBuild>, Ordered {

	private static final VersionRange NATIVE_NO_TEST_SUPPORT = VersionParser.DEFAULT
			.parseRange("[0.11.0-M1,0.11.0-M2]");

	private static final VersionRange NATIVE_011 = VersionParser.DEFAULT.parseRange("0.11.0-RC1");

	@Override
	public void customize(MavenBuild build) {
		Dependency dependency = build.dependencies().get("native");
		String springNativeVersion = dependency.getVersion().getValue();
		boolean hasTestSupport = !NATIVE_NO_TEST_SUPPORT.match(VersionParser.DEFAULT.parse(springNativeVersion));
		boolean latestNativeBuildTools = NATIVE_011.match(VersionParser.DEFAULT.parse(springNativeVersion));

		// Native build tools
		String nativeBuildToolsVersion = SpringNativeBuildtoolsVersionResolver.resolve(springNativeVersion);
		if (nativeBuildToolsVersion != null) {
			build.properties().property("repackage.classifier", "");
		}

		// Expose a property
		build.properties().version(VersionProperty.of("spring-native.version"), springNativeVersion);

		// Update dependency to reuse the property
		build.dependencies().add("native",
				Dependency.from(dependency).version(VersionReference.ofProperty("spring-native.version")));

		// AOT plugin
		build.plugins().add("org.springframework.experimental", "spring-aot-maven-plugin", (plugin) -> {
			plugin.version("${spring-native.version}");
			if (hasTestSupport) {
				plugin.execution("test-generate", (execution) -> execution.goal("test-generate"));
			}
			plugin.execution("generate", (execution) -> execution.goal("generate"));
		});

		// Spring Boot plugin
		build.plugins().add("org.springframework.boot", "spring-boot-maven-plugin",
				(plugin) -> plugin.configuration((configuration) -> {
					if (nativeBuildToolsVersion != null) {
						configuration.add("classifier", "${repackage.classifier}");
					}
					configuration.add("image", (image) -> {
						image.add("builder", "paketobuildpacks/builder:tiny");
						image.add("env", (env) -> env.add("BP_NATIVE_IMAGE", "true"));
					});
				}));

		if (build.dependencies().has("data-jpa")) {
			configureHibernateEnhancePlugin(build);
		}

		if (nativeBuildToolsVersion != null) {
			configureNativeProfile(build, hasTestSupport, latestNativeBuildTools, nativeBuildToolsVersion);
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 10;
	}

	private void configureHibernateEnhancePlugin(MavenBuild build) {
		build.plugins()
				.add("org.hibernate.orm.tooling", "hibernate-enhance-maven-plugin",
						(plugin) -> plugin.version("${hibernate.version}").execution("enhance",
								(execution) -> execution.goal("enhance").configuration((configuration) -> configuration
										.add("failOnError", "true").add("enableLazyInitialization", "true")
										.add("enableDirtyTracking", "true").add("enableAssociationManagement", "true")
										.add("enableExtendedEnhancement", "false"))));
	}

	private void configureNativeProfile(MavenBuild build, boolean hasTestSupport, boolean latestNativeBuildTools,
			String nativeBuildToolsVersion) {
		MavenProfile profile = build.profiles().id("native");
		profile.properties().version("native-buildtools.version", nativeBuildToolsVersion);
		profile.properties().property("repackage.classifier", "exec");
		if (hasTestSupport) {
			profile.dependencies().add("junit-platform-native", nativeTestDependency(latestNativeBuildTools));
		}
		profile.plugins().add("org.graalvm.buildtools", "native-maven-plugin", (plugin) -> {
			plugin.version("${native-buildtools.version}");
			if (latestNativeBuildTools) {
				plugin.extensions(true);
			}
			if (hasTestSupport) {
				plugin.execution("test-native", (execution) -> execution.goal("test").phase("test"));
			}
			plugin.execution("build-native", (execution) -> execution.goal("build").phase("package"));
		});
	}

	private Dependency nativeTestDependency(boolean latestNativeBuildTools) {
		if (latestNativeBuildTools) {
			return Dependency.withCoordinates("org.junit.platform", "junit-platform-launcher")
					.scope(DependencyScope.TEST_RUNTIME).build();
		}
		else {
			return Dependency.withCoordinates("org.graalvm.buildtools", "junit-platform-native")
					.version(VersionReference.ofProperty("native-buildtools.version"))
					.scope(DependencyScope.TEST_RUNTIME).build();
		}
	}

}
