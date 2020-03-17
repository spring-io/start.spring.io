/*
 * Copyright 2012-2020 the original author or authors.
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

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.spring.build.BuildMetadataResolver;
import io.spring.initializr.metadata.InitializrMetadata;

/**
 * A {@link BuildCustomizer} that automatically adds "reactor-kotlin-extensions" when a
 * dependency with the {@code reactive} facet is selected.
 *
 * @author Eddú Meléndez
 */
class ReactorKotlinExtensionsCustomizer implements BuildCustomizer<Build> {

	private final BuildMetadataResolver buildResolver;

	ReactorKotlinExtensionsCustomizer(InitializrMetadata metadata) {
		this.buildResolver = new BuildMetadataResolver(metadata);
	}

	@Override
	public void customize(Build build) {
		if (this.buildResolver.hasFacet(build, "reactive")) {
			build.dependencies().add("reactor-kotlin-extensions",
					Dependency.withCoordinates("io.projectreactor.kotlin", "reactor-kotlin-extensions"));
		}
	}

}
