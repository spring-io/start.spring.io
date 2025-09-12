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

package io.spring.start.site;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the application.
 *
 * @author Moritz Halbritter
 */
@ConfigurationProperties(prefix = "application")
public class StartConfigurationProperties {

	/**
	 * Configuration for the Maven version resolver.
	 */
	private final MavenVersionResolver mavenVersionResolver = new MavenVersionResolver();

	public MavenVersionResolver getMavenVersionResolver() {
		return this.mavenVersionResolver;
	}

	public static class MavenVersionResolver {

		/**
		 * Directory to use for the cache. If not set, a newly created temporary directory
		 * will be used.
		 */
		private String cacheDirectory;

		public String getCacheDirectory() {
			return this.cacheDirectory;
		}

		public void setCacheDirectory(String cacheDirectory) {
			this.cacheDirectory = cacheDirectory;
		}

	}

}
