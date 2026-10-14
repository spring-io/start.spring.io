/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.mybatis;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds
 * {@code mybatis-spring-boot-starter-test} when MyBatis is selected.
 *
 * @author Kazuki Shimizu
 */
public class MyBatisTestBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		Dependency mybatis = build.dependencies().get("mybatis");
		build.dependencies()
			.add("mybatis-test",
					Dependency.withCoordinates(mybatis.getGroupId(), mybatis.getArtifactId() + "-test")
						.version(mybatis.getVersion())
						.scope(DependencyScope.TEST_COMPILE));
	}

}
