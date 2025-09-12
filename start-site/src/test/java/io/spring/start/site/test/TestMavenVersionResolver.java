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
