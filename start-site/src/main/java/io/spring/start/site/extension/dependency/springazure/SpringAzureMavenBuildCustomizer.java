/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springazure;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that adds a Maven plugin when azure-support is selected.
 *
 * @author Muyao Feng
 */
class SpringAzureMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	private static final String PLUGIN_VERSION = "0.1.0";

	private final ProjectDescription projectDescription;

	SpringAzureMavenBuildCustomizer(ProjectDescription projectDescription) {
		this.projectDescription = projectDescription;
	}

	@Override
	public void customize(MavenBuild build) {
		build.plugins().add("com.microsoft.azure", "azure-container-apps-maven-plugin", (plugin) -> {
			plugin.version(PLUGIN_VERSION);
			plugin.configuration((configuration) -> {
				configuration.add("subscriptionId", "your-subscription-id");
				configuration.add("resourceGroup", "your-resource-group");
				configuration.add("appEnvironmentName", "your-app-environment-name");
				configuration.add("region", "your-region");
				configuration.add("appName", this.projectDescription.getName());
				configuration.add("containers", (containers) -> containers.add("container", (container) -> {
					container.add("type", "code");
					container.add("directory", "${project.basedir}");
				}));
				configuration.add("ingress", (ingress) -> {
					ingress.add("external", "true");
					ingress.add("targetPort", "8080");
				});
				configuration.add("scale", (scale) -> {
					scale.add("minReplicas", "0");
					scale.add("maxReplicas", "10");
				});
			});
		});
	}

}
