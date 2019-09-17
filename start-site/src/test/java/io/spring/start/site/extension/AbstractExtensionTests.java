/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension;

import java.util.Arrays;

import io.spring.initializr.generator.test.buildsystem.gradle.GroovyDslGradleBuildAssert;
import io.spring.initializr.generator.test.buildsystem.maven.MavenBuildAssert;
import io.spring.initializr.generator.test.project.ProjectStructure;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.project.DefaultProjectRequestToDescriptionConverter;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectGenerationResult;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.initializr.web.project.WebProjectRequest;
import org.assertj.core.api.AssertProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Base test class for extensions.
 *
 * @author Stephane Nicoll
 */
@SpringBootTest
public abstract class AbstractExtensionTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private InitializrMetadataProvider metadataProvider;

	private ProjectGenerationInvoker<ProjectRequest> invoker;

	private ProjectGenerationInvoker<ProjectRequest> getInvoker() {
		if (this.invoker == null) {
			this.invoker = new ProjectGenerationInvoker<>(this.applicationContext,
					new DefaultProjectRequestToDescriptionConverter());
		}
		return this.invoker;
	}

	protected InitializrMetadata getMetadata() {
		return this.metadataProvider.get();
	}

	protected Dependency getDependency(String id) {
		return getMetadata().getDependencies().get(id);
	}

	protected BillOfMaterials getBom(String id, String version) {
		BillOfMaterials bom = getMetadata().getConfiguration().getEnv().getBoms().get(id);
		return bom.resolve(Version.parse(version));
	}

	protected AssertProvider<MavenBuildAssert> mavenPom(ProjectRequest request) {
		request.setType("maven-build");
		String content = new String(getInvoker().invokeBuildGeneration(request));
		return () -> new MavenBuildAssert(content);
	}

	protected AssertProvider<GroovyDslGradleBuildAssert> gradleBuild(ProjectRequest request) {
		request.setType("gradle-build");
		String content = new String(getInvoker().invokeBuildGeneration(request));
		return () -> new GroovyDslGradleBuildAssert(content);
	}

	protected ProjectStructure generateProject(ProjectRequest request) {
		ProjectGenerationResult result = getInvoker().invokeProjectStructureGeneration(request);
		return new ProjectStructure(result.getRootDirectory());
	}

	protected ProjectRequest createProjectRequest(String... styles) {
		WebProjectRequest request = new WebProjectRequest();
		request.initialize(this.metadataProvider.get());
		request.getDependencies().addAll(Arrays.asList(styles));
		return request;
	}

}
