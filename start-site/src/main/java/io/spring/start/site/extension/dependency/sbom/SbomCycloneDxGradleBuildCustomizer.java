/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.sbom;

import java.util.ArrayList;
import java.util.List;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * {@link BuildCustomizer} that adds the CycloneDX Gradle plugin.
 *
 * @author Moritz Halbritter
 */
class SbomCycloneDxGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final ProjectDescription description;

	private final PluginVersionMapping pluginVersionMapping;

	SbomCycloneDxGradleBuildCustomizer(ProjectDescription description) {
		this.description = description;
		this.pluginVersionMapping = new PluginVersionMapping("3.5.1");
	}

	@Override
	public void customize(GradleBuild build) {
		String pluginVersion = this.pluginVersionMapping.getPluginVersion(this.description.getPlatformVersion());
		build.plugins().add("org.cyclonedx.bom", (plugin) -> plugin.setVersion(pluginVersion));
	}

	private static final class PluginVersionMapping {

		private final String defaultVersion;

		private final List<Mapping> mappings = new ArrayList<>();

		PluginVersionMapping(String defaultVersion) {
			this.defaultVersion = defaultVersion;
		}

		void addVersion(String bootVersion, String pluginVersion) {
			this.mappings.add(new Mapping(VersionParser.DEFAULT.parseRange(bootVersion), pluginVersion));
		}

		String getPluginVersion(Version bootVersion) {
			for (Mapping mapping : this.mappings) {
				if (mapping.bootVersion().match(bootVersion)) {
					return mapping.pluginVersion();
				}
			}
			return this.defaultVersion;
		}

		private record Mapping(VersionRange bootVersion, String pluginVersion) {
		}

	}

}
