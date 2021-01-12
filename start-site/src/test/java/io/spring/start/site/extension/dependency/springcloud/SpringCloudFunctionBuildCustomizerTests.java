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

import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringCloudFunctionBuildCustomizer}.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
class SpringCloudFunctionBuildCustomizerTests extends AbstractExtensionTests {

	static final Dependency WEB_ADAPTER = Dependency.withId("cloud-function-web", "org.springframework.cloud",
			"spring-cloud-function-web");

	@Test
	void functionOnly() {
		ProjectRequest request = createProjectRequest("cloud-function");
		assertThat(mavenPom(request)).hasDependency(getDependency("cloud-function"))
				.hasDependency(Dependency.createSpringBootStarter("test", Dependency.SCOPE_TEST))
				.hasDependency(Dependency.createSpringBootStarter("")).hasDependenciesSize(3)
				.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
				.hasBomsSize(1);
	}

	@Test
	void web() {
		ProjectRequest request = createProjectRequest("web", "cloud-function");
		BillOfMaterials bom = getBom("spring-cloud", request.getBootVersion());
		assertThat(mavenPom(request)).hasDependency(getDependency("web")).hasDependency(WEB_ADAPTER)
				.hasDependenciesSize(3)
				.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
				.hasBomsSize(1).hasProperty("spring-cloud.version", bom.getVersion());
	}

	@Test
	void webflux() {
		ProjectRequest request = createProjectRequest("webflux", "cloud-function");
		BillOfMaterials bom = getBom("spring-cloud", request.getBootVersion());
		assertThat(mavenPom(request)).hasDependency(getDependency("webflux")).hasDependency(WEB_ADAPTER)
				.hasDependenciesSize(4)
				.hasBom("org.springframework.cloud", "spring-cloud-dependencies", "${spring-cloud.version}")
				.hasBomsSize(1).hasProperty("spring-cloud.version", bom.getVersion());
	}

}
