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

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * {@link BuildCustomizer} for Gradle builds to configure the buildpack builder.
 *
 * @author Moritz Halbritter
 */
class SpringKafkaStreamsGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private static final VersionRange SPRING_BOOT_3_5_OR_LATER = VersionParser.DEFAULT.parseRange("3.5.0");

	private final ProjectDescription description;

	private final char quote;

	SpringKafkaStreamsGradleBuildCustomizer(ProjectDescription description, char quote) {
		this.description = description;
		this.quote = quote;
	}

	@Override
	public void customize(GradleBuild build) {
		build.tasks().customize("bootBuildImage", (bootBuildImage) -> {
			if (isBoot35orLater()) {
				bootBuildImage.attribute("runImage",
						this.quote + "paketobuildpacks/ubuntu-noble-run-base:latest" + this.quote);
			}
			else {
				bootBuildImage.attribute("builder",
						this.quote + "paketobuildpacks/builder-jammy-base:latest" + this.quote);
			}
		});
	}

	private boolean isBoot35orLater() {
		return SPRING_BOOT_3_5_OR_LATER.match(this.description.getPlatformVersion());
	}

}
