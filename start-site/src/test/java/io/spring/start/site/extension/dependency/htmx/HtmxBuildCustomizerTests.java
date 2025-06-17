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

package io.spring.start.site.extension.dependency.htmx;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.SupportedBootVersion;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HtmxBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class HtmxBuildCustomizerTests extends AbstractExtensionTests {

	private static final SupportedBootVersion BOOT_VERSION = SupportedBootVersion.V3_4;

	private final Dependency htmx = Dependency.withId("htmx", "io.github.wimdeblauwe", "htmx-spring-boot");

	private final Dependency htmxThymeleaf = Dependency.withId("htmx", "io.github.wimdeblauwe",
			"htmx-spring-boot-thymeleaf");

	@Test
	void shouldUseHtmxThymleafIfThymeleafIsSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "htmx", "thymeleaf");
		assertThat(mavenPom(request)).doesNotHaveDependency(this.htmx.getGroupId(), this.htmx.getArtifactId())
			.hasDependency(this.htmxThymeleaf);
	}

	@Test
	void shouldUsePlainHtmxIfThymeleafIsNotSelected() {
		ProjectRequest request = createProjectRequest(BOOT_VERSION, "htmx");
		assertThat(mavenPom(request))
			.doesNotHaveDependency(this.htmxThymeleaf.getGroupId(), this.htmxThymeleaf.getArtifactId())
			.hasDependency(this.htmx);
	}

	@Test
	void shouldNotAddHtmx() {
		ProjectRequest request = createProjectRequest("web");
		assertThat(mavenPom(request)).doesNotHaveDependency(this.htmx.getGroupId(), this.htmx.getArtifactId())
			.doesNotHaveDependency(this.htmxThymeleaf.getGroupId(), this.htmxThymeleaf.getArtifactId());
	}

}
