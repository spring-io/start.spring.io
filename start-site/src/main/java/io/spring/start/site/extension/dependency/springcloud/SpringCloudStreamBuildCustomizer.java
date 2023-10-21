/*
 * Copyright 2012-2023 the original author or authors.
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
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * Determine the appropriate Spring Cloud stream dependency to use based on the selected
 * integration technology.
 * <p>
 * Does not replace the integration technology jar by the relevant binder. If more than
 * one tech is selected, it is far easier to remove the unnecessary binder jar than to
 * figure out the name of the tech jar to add to keep support for that technology.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Brian Clozel
 */
class SpringCloudStreamBuildCustomizer implements BuildCustomizer<Build> {

	private final ProjectDescription description;

	SpringCloudStreamBuildCustomizer(ProjectDescription description) {
		this.description = description;
	}

	@Override
	public void customize(Build build) {
		if (hasDependency("cloud-stream", build) || hasDependency("cloud-bus", build)) {
			if (hasDependency("amqp", build)) {
				build.dependencies()
					.add("cloud-stream-binder-rabbit", "org.springframework.cloud", "spring-cloud-stream-binder-rabbit",
							DependencyScope.COMPILE);
			}
			if (hasDependency("kafka", build)) {
				build.dependencies()
					.add("cloud-stream-binder-kafka", "org.springframework.cloud", "spring-cloud-stream-binder-kafka",
							DependencyScope.COMPILE);
			}
			if (isSpringBoot3xWithPulsarSupport() && hasDependency("pulsar", build)) {
				build.dependencies()
					.add("cloud-stream-binder-pulsar", "org.springframework.cloud", "spring-cloud-stream-binder-pulsar",
							DependencyScope.COMPILE);
			}
		}
		// Spring Cloud Stream specific
		if (hasDependency("cloud-stream", build)) {
			if (hasDependency("kafka-streams", build)) {
				build.dependencies()
					.add("cloud-stream-binder-kafka-streams", "org.springframework.cloud",
							"spring-cloud-stream-binder-kafka-streams", DependencyScope.COMPILE);
			}
			if (isSpringBoot3x()) {
				build.dependencies()
					.add("cloud-stream-test",
							Dependency.withCoordinates("org.springframework.cloud", "spring-cloud-stream-test-binder")
								.scope(DependencyScope.TEST_COMPILE));
			}
			else if (build instanceof MavenBuild) {
				// TODO: https://github.com/spring-io/initializr/issues/1159
				build.dependencies()
					.add("cloud-stream-test",
							Dependency.withCoordinates("org.springframework.cloud", "spring-cloud-stream")
								.classifier("test-binder")
								.type("test-jar")
								.scope(DependencyScope.TEST_COMPILE));
			}
		}
	}

	protected boolean hasDependency(String id, Build build) {
		return build.dependencies().has(id);
	}

	protected boolean isSpringBoot3x() {
		Version platformVersion = this.description.getPlatformVersion();
		return platformVersion.compareTo(Version.parse("3.0.0-M1")) > 0;
	}

	protected boolean isSpringBoot3xWithPulsarSupport() {
		Version platformVersion = this.description.getPlatformVersion();
		return platformVersion.compareTo(Version.parse("3.2.0-M3")) >= 0;
	}

}
