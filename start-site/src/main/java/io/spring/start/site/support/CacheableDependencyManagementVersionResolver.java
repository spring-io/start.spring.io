/*
 * Copyright 2012-2019 the original author or authors.
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

import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;

import org.springframework.cache.annotation.Cacheable;

/**
 * A {@link DependencyManagementVersionResolver} that uses the metadata cache to store
 * dependency management resolution.
 *
 * @author Stephane Nicoll
 */
public class CacheableDependencyManagementVersionResolver implements DependencyManagementVersionResolver {

	private final DependencyManagementVersionResolver delegate;

	public CacheableDependencyManagementVersionResolver(DependencyManagementVersionResolver delegate) {
		this.delegate = delegate;
	}

	@Override
	@Cacheable("initializr.metadata")
	public Map<String, String> resolve(String groupId, String artifactId, String version) {
		return this.delegate.resolve(groupId, artifactId, version);
	}

}
