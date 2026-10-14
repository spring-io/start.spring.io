/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springrestdocs;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that tunes the REST Docs dependency based on the web
 * framework that the application is using.
 *
 * @author Andy Wilkinson
 */
public class SpringRestDocsBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		build.dependencies()
			.add("spring-boot-restdocs", "org.springframework.boot", "spring-boot-starter-restdocs",
					DependencyScope.TEST_COMPILE);
		if (switchToWebTestClient(build)) {
			build.dependencies().remove("restdocs");
			build.dependencies()
				.add("restdocs-webtestclient", "org.springframework.restdocs", "spring-restdocs-webtestclient",
						DependencyScope.TEST_COMPILE);
		}
	}

	private boolean switchToWebTestClient(Build build) {
		if (build.dependencies().has("web")) {
			return false;
		}
		if (build.dependencies().has("webflux") || build.dependencies().has("jersey")) {
			return true;
		}
		return false;
	}

}
