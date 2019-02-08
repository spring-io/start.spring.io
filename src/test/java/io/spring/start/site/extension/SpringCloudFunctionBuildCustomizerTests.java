/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.WebProjectRequest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SpringCloudFunctionBuildCustomizer}.
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 */
class SpringCloudFunctionBuildCustomizerTests extends AbstractExtensionTests {

	static final Dependency SCS_ADAPTER = Dependency.withId("cloud-function-stream",
			"org.springframework.cloud", "spring-cloud-function-stream");

	static final Dependency WEB_ADAPTER = Dependency.withId("cloud-function-web",
			"org.springframework.cloud", "spring-cloud-function-web");

	@Test
	void functionOnly() {
		WebProjectRequest request = createProjectRequest("cloud-function");
		generateMavenPom(request).hasDependency(getDependency("cloud-function"))
				.hasSpringBootStarterTest().hasDependenciesCount(2);
	}

	@Test
	void springCloudStreamWithRabbit() {
		WebProjectRequest request = createProjectRequest("cloud-stream", "amqp",
				"cloud-function");
		request.setBootVersion("2.0.6.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("cloud-stream"))
				.hasDependency(getDependency("amqp")).hasDependency(SCS_ADAPTER)
				.hasDependenciesCount(6);
	}

	@Test
	void reactiveSpringCloudStreamWithKafka() {
		WebProjectRequest request = createProjectRequest("reactive-cloud-stream", "kafka",
				"cloud-function");
		request.setBootVersion("2.0.6.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("reactive-cloud-stream"))
				.hasDependency(getDependency("kafka")).hasDependency(SCS_ADAPTER)
				.hasDependenciesCount(7);
	}

	@Test
	void springCloudStreamWith21() {
		WebProjectRequest request = createProjectRequest("cloud-stream", "cloud-function");
		request.setBootVersion("2.1.0.RC1");
		generateMavenPom(request).hasDependency(getDependency("cloud-stream"))
				.hasDependency(getDependency("cloud-function")).hasDependenciesCount(4);
	}

	@Test
	void reactiveSpringCloudStreamWith21() {
		WebProjectRequest request = createProjectRequest("reactive-cloud-stream",
				"cloud-function");
		request.setBootVersion("2.1.0.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("reactive-cloud-stream"))
				.hasDependency(getDependency("cloud-function")).hasDependenciesCount(4);
	}

	@Test
	void web() {
		WebProjectRequest request = createProjectRequest("web", "cloud-function");
		generateMavenPom(request).hasDependency(getDependency("web"))
				.hasDependency(WEB_ADAPTER).hasDependenciesCount(3);
	}

	@Test
	void webflux() {
		WebProjectRequest request = createProjectRequest("webflux", "cloud-function");
		request.setBootVersion("2.1.0.BUILD-SNAPSHOT");
		generateMavenPom(request).hasDependency(getDependency("webflux"))
				.hasDependency(WEB_ADAPTER).hasDependenciesCount(4);
	}

	@Test
	void webfluxNotAvailableIn20() {
		WebProjectRequest request = createProjectRequest("webflux", "cloud-function");
		request.setBootVersion("2.0.5.RELEASE");
		generateMavenPom(request).hasDependency(getDependency("webflux"))
				.hasDependency(getDependency("cloud-function")).hasDependenciesCount(4);
	}

}
