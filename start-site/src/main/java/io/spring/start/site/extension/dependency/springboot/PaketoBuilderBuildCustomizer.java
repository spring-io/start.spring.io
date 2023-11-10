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

package io.spring.start.site.extension.dependency.springboot;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * {@link BuildCustomizer} base implementation for Paketo Builder.
 *
 * @param <B> the type of build
 * @author Stephane Nicoll
 */
abstract class PaketoBuilderBuildCustomizer<B extends Build> implements BuildCustomizer<B> {

	static final String BASE_IMAGE_BUILDER = "paketobuildpacks/builder-jammy-base:latest";

	static final String TINY_IMAGE_BUILDER = "paketobuildpacks/builder-jammy-tiny:latest";

	@Override
	public void customize(B build) {
		String builder = (build.dependencies().has("native")) ? TINY_IMAGE_BUILDER : BASE_IMAGE_BUILDER;
		customize(build, builder);
	}

	/**
	 * Customize the build with the specified image builder.
	 * @param build the build to customize
	 * @param imageBuilder the image builder to use
	 */
	protected abstract void customize(B build, String imageBuilder);

}
