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

package io.spring.start.site.test;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

/**
 * A parallel execution strategy for JUnit, which limits the maximum number of
 * parallelism.
 *
 * @author Moritz Halbritter
 */
public class JunitMaxParallelismStrategy implements ParallelExecutionConfigurationStrategy {

	private static final int MAX_PARALLELISM = 4;

	@Override
	public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
		int parallelism = Runtime.getRuntime().availableProcessors();
		if (parallelism > MAX_PARALLELISM) {
			parallelism = MAX_PARALLELISM;
		}
		return new FixedParallelExecutionConfiguration(parallelism);
	}

	private static final class FixedParallelExecutionConfiguration implements ParallelExecutionConfiguration {

		private final int processors;

		FixedParallelExecutionConfiguration(int processors) {
			this.processors = processors;
		}

		@Override
		public int getParallelism() {
			return this.processors;
		}

		@Override
		public int getMinimumRunnable() {
			return getParallelism();
		}

		@Override
		public int getMaxPoolSize() {
			return getParallelism();
		}

		@Override
		public int getCorePoolSize() {
			return getParallelism();
		}

		@Override
		public int getKeepAliveSeconds() {
			return 30;
		}

		@Override
		public Predicate<? super ForkJoinPool> getSaturatePredicate() {
			return (ignore) -> true;
		}

	}

}
