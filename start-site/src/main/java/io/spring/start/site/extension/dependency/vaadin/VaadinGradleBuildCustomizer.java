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

package io.spring.start.site.extension.dependency.vaadin;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradleDependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that registers Vaadin's Gradle plugin.
 *
 * @author Stephane Nicoll
 */
class VaadinGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private static final VersionRange SPRING_BOOT_4_OR_LATER = VersionParser.DEFAULT.parseRange("4.0.0");

	private final Version springBootVersion;

	private final String vaadinVersion;

	VaadinGradleBuildCustomizer(InitializrMetadata metadata, Version platformVersion) {
		this.springBootVersion = platformVersion;
		this.vaadinVersion = metadata.getConfiguration()
			.getEnv()
			.getBoms()
			.get("vaadin")
			.resolve(platformVersion)
			.getVersion();
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("com.vaadin", (plugin) -> plugin.setVersion(this.vaadinVersion));
		if (isBoot4OrLater()) {
			build.dependencies()
				.add("vaadin-dev",
						GradleDependency.withCoordinates("com.vaadin", "vaadin-dev").configuration("developmentOnly"));
		}
	}

	private boolean isBoot4OrLater() {
		return SPRING_BOOT_4_OR_LATER.match(this.springBootVersion);
	}

}
