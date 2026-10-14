/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.test;

import java.util.Map;

import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.testsupport.TemporaryFiles;

/**
 * A {@link MavenVersionResolver} for tests, which uses a fixed directory for the cache.
 *
 * @author Moritz Halbritter
 */
public class TestMavenVersionResolver implements MavenVersionResolver {

	private static final TestMavenVersionResolver INSTANCE = new TestMavenVersionResolver();

	private final MavenVersionResolver delegate = MavenVersionResolver
		.withCacheLocation(TemporaryFiles.getTempDir().resolve("maven-version-resolver-cache"));

	@Override
	public Map<String, String> resolveDependencies(String groupId, String artifactId, String version) {
		return this.delegate.resolveDependencies(groupId, artifactId, version);
	}

	@Override
	public Map<String, String> resolvePlugins(String groupId, String artifactId, String version) {
		return this.delegate.resolvePlugins(groupId, artifactId, version);
	}

	public static TestMavenVersionResolver get() {
		return INSTANCE;
	}

}
