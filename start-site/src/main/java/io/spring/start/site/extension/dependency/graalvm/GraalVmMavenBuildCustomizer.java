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

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} for Maven projects using GraalVM.
 *
 * @author Stephane Nicoll
 */
class GraalVmMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		// Spring Boot plugin
		build.plugins().add("org.springframework.boot", "spring-boot-maven-plugin",
				(plugin) -> plugin.configuration((configuration) -> configuration.add("image", (image) -> {
					image.add("buildpacks", (buildpacks) -> {
						buildpacks.add("buildpack", "gcr.io/paketo-buildpacks/bellsoft-liberica:9.9.0-ea");
						buildpacks.add("buildpack", "gcr.io/paketo-buildpacks/java-native-image");
					});
				})));

		if (build.dependencies().has("data-jpa")) {
			configureHibernateEnhancePlugin(build);
		}
	}

	private void configureHibernateEnhancePlugin(MavenBuild build) {
		build.plugins()
				.add("org.hibernate.orm.tooling", "hibernate-enhance-maven-plugin",
						(plugin) -> plugin.version("${hibernate.version}").execution("enhance",
								(execution) -> execution.goal("enhance").configuration((configuration) -> configuration
										.add("enableLazyInitialization", "true").add("enableDirtyTracking", "true")
										.add("enableAssociationManagement", "true"))));
	}

}
