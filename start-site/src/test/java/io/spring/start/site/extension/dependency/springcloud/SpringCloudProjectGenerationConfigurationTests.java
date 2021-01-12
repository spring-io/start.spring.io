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
import io.spring.initializr.generator.buildsystem.BuildSystem;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.test.project.ProjectAssetTester;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringCloudProjectGenerationConfiguration}.
 *
 * @author Stephane Nicoll
 */
@SpringBootTest
class SpringCloudProjectGenerationConfigurationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private InitializrMetadataProvider metadataProvider;

	private final ProjectAssetTester projectTester = new ProjectAssetTester()
			.withContextInitializer((context) -> context.setParent(this.applicationContext))
			.withBean(InitializrMetadata.class, () -> this.metadataProvider.get())
			.withBean(Build.class, MavenBuild::new).withConfiguration(SpringCloudProjectGenerationConfiguration.class);

	@Test
	void springCloudContractGradleBuildCustomizerWithGroovyDsl() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setBuildSystem(BuildSystem.forIdAndDialect(GradleBuildSystem.ID, GradleBuildSystem.DIALECT_GROOVY));
		description.addDependency("cloud-contract-verifier", mock(Dependency.class));
		this.projectTester.configure(description,
				(context) -> assertThat(context).hasSingleBean(SpringCloudContractGradleBuildCustomizer.class));
	}

	@Test
	void springCloudContractGradleBuildCustomizerWithKotlinDsl() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.setBuildSystem(BuildSystem.forIdAndDialect(GradleBuildSystem.ID, GradleBuildSystem.DIALECT_KOTLIN));
		description.addDependency("cloud-contract-verifier", mock(Dependency.class));
		this.projectTester.configure(description,
				(context) -> assertThat(context).doesNotHaveBean(SpringCloudContractGradleBuildCustomizer.class));
	}

}
