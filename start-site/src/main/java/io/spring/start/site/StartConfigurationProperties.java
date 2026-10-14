/*
 * Copyright 2012 - present the original author or authors.
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
