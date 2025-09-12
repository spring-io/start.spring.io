/*
 * Copyright 2012 - present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension.dependency.tanzu.spring.enterprise;

import java.util.Set;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} to add the 'cloud-gateway-reactive' dependency if at least one
 * of the Tanzu Spring Enterprise Spring Cloud Gateway extensions is selected.
 *
 * @author Moritz Halbritter
 */
class TanzuEnterpriseScgExtensionsBuildCustomizer implements BuildCustomizer<Build> {

	private static final Set<String> EXTENSION_DEPENDENCIES = Set.of("tanzu-scg-access-control", "tanzu-scg-custom",
			"tanzu-scg-graphql", "tanzu-scg-sso", "tanzu-scg-traffic-control", "tanzu-scg-transformation");

	@Override
	public void customize(Build build) {
		if (!hasExtensionDependency(build)) {
			return;
		}
		addScgIfNecessary(build);
	}

	private void addScgIfNecessary(Build build) {
		if (!build.dependencies().has("cloud-gateway-reactive")) {
			build.dependencies().add("cloud-gateway-reactive");
		}
	}

	private boolean hasExtensionDependency(Build build) {
		for (String extensionDependency : EXTENSION_DEPENDENCIES) {
			if (build.dependencies().has(extensionDependency)) {
				return true;
			}
		}
		return false;
	}

}
