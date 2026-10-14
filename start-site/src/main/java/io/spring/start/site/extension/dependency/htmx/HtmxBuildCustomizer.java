/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.htmx;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that replaces {@code htmx-spring-boot} with
 * {@code htmx-spring-boot-thymeleaf} if Thymeleaf is selected.
 *
 * @author Moritz Halbritter
 */
class HtmxBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("thymeleaf")) {
			Dependency htmx = build.dependencies().get("htmx");
			build.dependencies().remove("htmx");
			build.dependencies()
				.add("htmx-thymeleaf",
						Dependency.withCoordinates(htmx.getGroupId(), "htmx-spring-boot-thymeleaf")
							.version(htmx.getVersion())
							.build());
		}
	}

}
