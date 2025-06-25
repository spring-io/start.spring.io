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

package io.spring.start.site.extension.dependency.springkafka;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * {@link BuildCustomizer} for Maven builds to configure the buildpack builder.
 *
 * @author Moritz Halbritter
 */
class SpringKafkaStreamsMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private static final VersionRange SPRING_BOOT_3_5_OR_LATER = VersionParser.DEFAULT.parseRange("3.5.0");

	private final ProjectDescription description;

	SpringKafkaStreamsMavenBuildCustomizer(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(MavenBuild build) {
		build.plugins()
			.add("org.springframework.boot", "spring-boot-maven-plugin",
					(plugin) -> plugin.configuration((configuration) -> configuration.configure("image", (image) -> {
						if (isBoot35orLater()) {
							image.add("runImage", "paketobuildpacks/ubuntu-noble-run-base:latest");
						}
						else {
							image.add("builder", "paketobuildpacks/builder-jammy-base:latest");
						}
					})));
	}

	private boolean isBoot35orLater() {
		return SPRING_BOOT_3_5_OR_LATER.match(this.description.getPlatformVersion());
	}

}
