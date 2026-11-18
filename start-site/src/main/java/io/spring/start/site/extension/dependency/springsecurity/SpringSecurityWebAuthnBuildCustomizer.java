/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
 */

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that ensures that Spring Security is added if Spring Security
 * WebAuthn is selected.
 *
 * @author Moritz Halbritter
 */
public class SpringSecurityWebAuthnBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		if (!build.dependencies().has("security")) {
			build.dependencies().add("security");
		}
	}

}
