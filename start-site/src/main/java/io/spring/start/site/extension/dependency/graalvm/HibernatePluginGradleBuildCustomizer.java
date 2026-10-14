/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * A {@link BuildCustomizer} for projects using the Groovy DSL with GraalVm and Hibernate.
 *
 * @author Stephane Nicoll
 * @author Taewoong Kim
 */
class HibernatePluginGradleBuildCustomizer implements BuildCustomizer<GradleBuild> {

	private final Version hibernateVersion;

	HibernatePluginGradleBuildCustomizer(Version hibernateVersion) {
		this.hibernateVersion = hibernateVersion;
	}

	@Override
	public void customize(GradleBuild build) {
		build.plugins().add("org.hibernate.orm", (plugin) -> plugin.setVersion(this.hibernateVersion.toString()));
		build.extensions().customize("hibernate", (hibernate) -> hibernate.nested("enhancement", (enhancement) -> {
		}));
	}

}
