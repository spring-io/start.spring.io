/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} for Maven projects using GraalVm and Hibernate.
 *
 * @author Stephane Nicoll
 * @author Moritz Halbritter
 * @author Taewoong Kim
 */
// See https://docs.hibernate.org/orm/7.1/userguide/html_single/#BytecodeEnhancement
class HibernatePluginMavenBuildCustomizer implements BuildCustomizer<MavenBuild> {

	@Override
	public void customize(MavenBuild build) {
		build.plugins()
			.add("org.hibernate.orm", "hibernate-maven-plugin", (plugin) -> plugin.version("${hibernate.version}")
				.execution("enhance", (execution) -> execution.goal("enhance")));
	}

}
