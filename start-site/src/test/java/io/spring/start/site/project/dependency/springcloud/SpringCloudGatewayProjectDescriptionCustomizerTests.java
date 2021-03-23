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

package io.spring.start.site.project.dependency.springcloud;

import java.util.Collections;

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.MutableProjectDescription;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Tests for {@link SpringCloudGatewayProjectDescriptionCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringCloudGatewayProjectDescriptionCustomizerTests {

	@Test
	void customizeWithSpringCloudGatewayAndSpringMvcMigratesToSpringWebFlux() {
		MutableProjectDescription description = new MutableProjectDescription();
		description.addDependency("cloud-gateway", mock(Dependency.class));
		description.addDependency("web", mock(Dependency.class));
		new SpringCloudGatewayProjectDescriptionCustomizer().customize(description);
		assertThat(description.getRequestedDependencies()).containsOnlyKeys("cloud-gateway", "webflux");
		Dependency webflux = description.getRequestedDependencies().get("webflux");
		assertThat(webflux.getGroupId()).isEqualTo("org.springframework.boot");
		assertThat(webflux.getArtifactId()).isEqualTo("spring-boot-starter-webflux");
	}

	@Test
	void customizeWithSpringCloudGatewayDoesNotAddSpringWebFlux() {
		MutableProjectDescription description = mock(MutableProjectDescription.class);
		given(description.getRequestedDependencies())
				.willReturn(Collections.singletonMap("cloud-gateway", mock(Dependency.class)));
		new SpringCloudGatewayProjectDescriptionCustomizer().customize(description);
		verify(description).getRequestedDependencies();
		verifyNoMoreInteractions(description);
	}

	@Test
	void customizeWithoutSpringCloudGatewayDoesNotRemoveSpringMvc() {
		MutableProjectDescription description = mock(MutableProjectDescription.class);
		given(description.getRequestedDependencies())
				.willReturn(Collections.singletonMap("web", mock(Dependency.class)));
		new SpringCloudGatewayProjectDescriptionCustomizer().customize(description);
		verify(description).getRequestedDependencies();
		verifyNoMoreInteractions(description);
	}

}
