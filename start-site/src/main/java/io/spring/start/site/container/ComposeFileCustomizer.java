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

package io.spring.start.site.container;

import io.spring.initializr.generator.container.docker.compose.ComposeFile;

import org.springframework.core.Ordered;

/**
 * Callback for customizing a project's {@link ComposeFile}. Invoked with an
 * {@link Ordered order} of {@code 0} by default, considering overriding
 * {@link #getOrder()} to customize this behaviour.
 *
 * @author Moritz Halbritter
 */
@FunctionalInterface
public interface ComposeFileCustomizer extends Ordered {

	/**
	 * Customizes the given {@link ComposeFile}.
	 * @param composeFile the compose file to customize
	 */
	void customize(ComposeFile composeFile);

	@Override
	default int getOrder() {
		return 0;
	}

}
