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

package io.spring.start.site.extension.code.kotlin;

import java.util.function.Function;

import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.test.TestMavenVersionResolver;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests for {@link ManagedDependenciesKotlinVersionResolver}.
 *
 * @author Andy Wilkinson
 */
class ManagedDependenciesKotlinVersionResolverTests {

	private final MavenVersionResolver mavenVersionResolver = TestMavenVersionResolver.get();

	@Test
	@SuppressWarnings("unchecked")
	void kotlinVersionCanBeResolved() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("2.5.0"));
		Function<ProjectDescription, String> fallback = mock(Function.class);
		String version = new ManagedDependenciesKotlinVersionResolver(this.mavenVersionResolver, fallback)
			.resolveKotlinVersion(description);
		assertThat(version).isEqualTo("1.5.0");
		verifyNoInteractions(fallback);
	}

}
