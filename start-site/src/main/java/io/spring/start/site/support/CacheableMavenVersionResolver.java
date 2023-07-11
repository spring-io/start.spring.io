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

import java.lang.reflect.Method;
import java.util.Map;

import io.spring.initializr.versionresolver.MavenVersionResolver;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.KeyGenerator;

/**
 * A {@link MavenVersionResolver} that uses the metadata cache to store dependency and
 * plugin management resolution.
 *
 * @author Stephane Nicoll
 */
@CacheConfig(cacheNames = "initializr.metadata", keyGenerator = "mavenVersionResolver")
public class CacheableMavenVersionResolver implements MavenVersionResolver, KeyGenerator {

	private final MavenVersionResolver delegate;

	public CacheableMavenVersionResolver(MavenVersionResolver delegate) {
		this.delegate = delegate;
	}

	@Override
	@Cacheable
	public Map<String, String> resolveDependencies(String groupId, String artifactId, String version) {
		return this.delegate.resolveDependencies(groupId, artifactId, version);
	}

	@Override
	@Cacheable
	public Map<String, String> resolvePlugins(String groupId, String artifactId, String version) {
		return this.delegate.resolvePlugins(groupId, artifactId, version);
	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		String prefix = (method.getName().equals("resolveDependencies")) ? "dependencies" : "plugins";
		return "%s-%s:%s:%s".formatted(prefix, params[0], params[1], params[2]);
	}

}
