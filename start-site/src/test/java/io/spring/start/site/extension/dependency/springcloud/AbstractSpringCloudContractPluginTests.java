/*
 * Copyright 2012-2021 the original author or authors.
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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Abstract base class for unit testing Spring Cloud Contract build customizers.
 *
 * @author Madhura Bhave
 */
abstract class AbstractSpringCloudContractPluginTests<T extends Build> {

	private BuildCustomizer<T> customizer;

	private SpringCloudProjectVersionResolver resolver;

	@BeforeEach
	void setup() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setPlatformVersion(Version.parse("2.2.2.RELEASE"));
		this.resolver = mock(SpringCloudProjectVersionResolver.class);
		this.customizer = getCustomizer(description, this.resolver);
	}

	protected abstract BuildCustomizer<T> getCustomizer(MutableProjectDescription description,
			SpringCloudProjectVersionResolver resolver);

	protected abstract T getBuild();

	@Test
	void customizerShouldConfigureMilestonePluginRepoWhenVersionMilestone() {
		given(this.resolver.resolveVersion(any(), any())).willReturn("2.2.0.M3");
		T build = getBuild();
		this.customizer.customize(build);
		MavenRepository milestoneRepo = build.pluginRepositories().get("spring-milestones");
		assertThat(milestoneRepo.getName()).isEqualTo("Spring Milestones");
		assertThat(milestoneRepo.getUrl()).isEqualTo("https://repo.spring.io/milestone");
	}

	@Test
	void customizerShouldConfigureMilestoneAndSnapshotsPluginRepoWhenVersionMilestone() {
		given(this.resolver.resolveVersion(any(), any())).willReturn("2.2.0.BUILD-SNAPSHOT");
		T build = getBuild();
		this.customizer.customize(build);
		MavenRepository snapshotsRepo = build.pluginRepositories().get("spring-snapshots");
		assertThat(snapshotsRepo.getName()).isEqualTo("Spring Snapshots");
		assertThat(snapshotsRepo.getUrl()).isEqualTo("https://repo.spring.io/snapshot");
		assertThat(snapshotsRepo.isSnapshotsEnabled()).isTrue();
		MavenRepository milestoneRepo = build.pluginRepositories().get("spring-milestones");
		assertThat(milestoneRepo.getName()).isEqualTo("Spring Milestones");
		assertThat(milestoneRepo.getUrl()).isEqualTo("https://repo.spring.io/milestone");
	}

	@Test
	void customizerShouldNotConfigurePluginsRepoWhenVersionRelease() {
		given(this.resolver.resolveVersion(any(), any())).willReturn("2.2.2.RELEASE");
		T build = getBuild();
		this.customizer.customize(build);
		assertThat(build.pluginRepositories().isEmpty()).isTrue();
	}

}
