/*
 * Copyright 2012 - present the original author or authors.
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
	public void close() {
		this.delegate.close();
	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		String prefix = (method.getName().equals("resolveDependencies")) ? "dependencies" : "plugins";
		return "%s-%s:%s:%s".formatted(prefix, params[0], params[1], params[2]);
	}

}
