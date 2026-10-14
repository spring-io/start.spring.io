/*
 * Copyright 2012 - present the original author or authors.
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
