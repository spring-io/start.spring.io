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

package io.spring.start.testsupport;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Homes}.
 *
 * @author Moritz Halbritter
 */
class HomesTests {

	@Test
	void shouldGetNewHome() {
		Homes test1 = new Homes("test1");
		Homes test1Again = new Homes("test1");
		Homes test2 = new Homes("test2");
		assertThat(test1.get()).isEmptyDirectory();
		assertThat(test1.get()).isEqualTo(test1.get());
		assertThat(test1.get()).isEqualTo(test1Again.get());
		assertThat(test1.get()).isNotEqualTo(test2.get());
	}

}
