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

package io.spring.start.site.extension.dependency.springnative;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.MavenRepository;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that registers the necessary {@link MavenRepository} for
 * Spring Native. As the Gradle plugin automatically adds the Spring Native dependency, we
 * need to include the repository manually.
 *
 * @author Stephane Nicoll
 */
class SpringNativeRepositoryBuildCustomizer implements BuildCustomizer<Build> {

	private final MavenRepository mavenRepository;

	SpringNativeRepositoryBuildCustomizer(MavenRepository mavenRepository) {
		this.mavenRepository = mavenRepository;
	}

	@Override
	public void customize(Build build) {
		if (this.mavenRepository != null) {
			build.repositories().add(this.mavenRepository);
			build.pluginRepositories().add(this.mavenRepository);
		}
	}

}
