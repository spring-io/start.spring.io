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

package io.spring.start.testsupport;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Homes}.
 *
 * @author Moritz Halbritter
 */
class HomesTests {

	@Test
	void shouldAcquireNewHome() {
		Homes homes = new Homes("test");
		Path home1 = homes.acquire();
		Path home2 = homes.acquire();
		assertThat(home1).isNotEqualTo(home2);
		homes.release(home1);
		Path home3 = homes.acquire();
		assertThat(home3).isEqualTo(home1);
	}

}
