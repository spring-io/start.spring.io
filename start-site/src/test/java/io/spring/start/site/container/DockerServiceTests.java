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

package io.spring.start.site.container;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DockerService}.
 *
 * @author Chris Bono
 */
class DockerServiceTests {

	@Test
	void builderWithMinimalOptions() {
		DockerService service = DockerService.withImageAndTag("acme/toolbox").build();
		assertThat(service.getImage()).isEqualTo("acme/toolbox");
		assertThat(service.getImageTag()).isEqualTo("latest");
		assertThat(service.getWebsite()).isNull();
		assertThat(service.getCommand()).isNull();
		assertThat(service.getPorts()).isEmpty();
	}

	@Test
	void builderWithAllOptions() {
		DockerService service = DockerService.withImageAndTag("acme/toolbox")
			.imageTag("1.0")
			.website("acme/toolbox-dot-com")
			.command("bin/acme run")
			.ports(8007, 8008)
			.build();
		assertThat(service.getImage()).isEqualTo("acme/toolbox");
		assertThat(service.getImageTag()).isEqualTo("1.0");
		assertThat(service.getWebsite()).isEqualTo("acme/toolbox-dot-com");
		assertThat(service.getCommand()).isEqualTo("bin/acme run");
		assertThat(service.getPorts()).containsExactly(8007, 8008);
	}

	@Test
	void builderWithImageAndTagUsesTag() {
		DockerService service = DockerService.withImageAndTag("acme/toolbox:1.0").build();
		assertThat(service.getImage()).isEqualTo("acme/toolbox");
		assertThat(service.getImageTag()).isEqualTo("1.0");
	}

	@Test
	void builderWithImageAndTagIsOverriddenByImageTag() {
		DockerService service = DockerService.withImageAndTag("acme/toolbox:1.0").imageTag("2.0").build();
		assertThat(service.getImage()).isEqualTo("acme/toolbox");
		assertThat(service.getImageTag()).isEqualTo("2.0");
	}

	@Test
	void builderWithCollectionOfPorts() {
		DockerService service = DockerService.withImageAndTag("acme/toolbox").ports(List.of(8007, 8008)).build();
		assertThat(service.getPorts()).containsExactly(8007, 8008);
	}

}
