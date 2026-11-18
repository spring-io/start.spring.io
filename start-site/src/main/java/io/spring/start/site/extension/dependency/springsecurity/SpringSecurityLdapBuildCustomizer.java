/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that provides Spring Security's LDAP support when both LDAP
 * and Spring Security are selected.
 *
 * @author Moritz Halbritter
 */
public class SpringSecurityLdapBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (build.dependencies().has("ldap")) {
			build.dependencies()
				.add("security-ldap",
						Dependency.withCoordinates("org.springframework.security", "spring-security-ldap"));
		}
	}

}
