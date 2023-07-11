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

package io.spring.start.site.support;

import java.util.Map;

import io.spring.initializr.versionresolver.MavenVersionResolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CacheableMavenVersionResolver}.
 *
 * @author Stephane Nicoll
 */
@SpringBootTest
@AutoConfigureCache(cacheProvider = CacheType.SIMPLE)
class CacheableMavenVersionResolverTests {

	private final MavenVersionResolver versionResolver;

	private final Cache cache;

	CacheableMavenVersionResolverTests(@Autowired MavenVersionResolver versionResolver,
			@Autowired CacheManager cacheManager) {
		this.versionResolver = versionResolver;
		this.cache = cacheManager.getCache("initializr.metadata");
	}

	@BeforeEach
	@AfterEach
	void clearCache() {
		this.cache.clear();
	}

	@Test
	void managedDependenciesAreCached() {
		Map<String, String> dependencies = this.versionResolver.resolveDependencies("org.springframework.boot",
				"spring-boot-dependencies", "3.1.0");
		assertThat(dependencies).isNotNull();
		ValueWrapper valueWrapper = this.cache
			.get("dependencies-org.springframework.boot:spring-boot-dependencies:3.1.0");
		assertThat(valueWrapper).isNotNull();
		assertThat(valueWrapper.get()).isInstanceOf(Map.class);
	}

	@Test
	void managedPluginsAreCached() {
		Map<String, String> plugins = this.versionResolver.resolvePlugins("org.springframework.boot",
				"spring-boot-dependencies", "3.1.0");
		assertThat(plugins).isNotNull();
		ValueWrapper valueWrapper = this.cache.get("plugins-org.springframework.boot:spring-boot-dependencies:3.1.0");
		assertThat(valueWrapper).isNotNull();
		assertThat(valueWrapper.get()).isInstanceOf(Map.class);
	}

}
