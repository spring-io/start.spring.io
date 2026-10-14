/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.ldap;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to add
 * {@code org.springframework.boot:spring-boot-starter-ldap} if it isn't already present.
 *
 * @author Moritz Halbritter
 */
public class LdapUnboundIdBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		// The LDAP starter is required to use the embedded LDAP server. Only add it
		// if it isn't already present, to avoid duplicating it when the LDAP starter
		// was already selected.
		if (!build.dependencies().has("ldap")) {
			build.dependencies()
				.add("ldap", "org.springframework.boot", "spring-boot-starter-ldap", DependencyScope.TEST_COMPILE);
		}
	}

}
