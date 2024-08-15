/*
 * Copyright 2012-2024 the original author or authors.
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

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DockerServiceResolver}.
 *
 * @author Fer Clager
 */
class DockerServiceResolverTests {

	class MockDockerServiceResolver implements DockerServiceResolver {

		private final DockerService resolvedService;

		MockDockerServiceResolver(DockerService resolvedService) {
			this.resolvedService = resolvedService;
		}

		@Override
		public DockerService resolve(String id) {
			return this.resolvedService;
		}

	}

	@Test
	void doWith_withExistingService_callsConsumer() {
		// given
		DockerService mockService = mock(DockerService.class);
		Consumer<DockerService> serviceConsumer = mock(Consumer.class);
		DockerServiceResolver resolver = new MockDockerServiceResolver(mockService);

		// when
		resolver.doWith("existing-service", serviceConsumer);

		// then
		verify(serviceConsumer, times(1)).accept(mockService);
	}

	@Test
	void doWith_withNonExistingService_doesNothing() {
		// given
		Consumer<DockerService> serviceConsumer = mock(Consumer.class);
		DockerServiceResolver resolver = new MockDockerServiceResolver(null);

		// when
		resolver.doWith("non-existing-service", serviceConsumer);

		// then
		verify(serviceConsumer, never()).accept(any(DockerService.class));
	}

}
