/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site;

/**
 * Supported Spring Boot versions.
 *
 * @author Moritz Halbritter
 */
public enum SupportedBootVersion {

	/**
	 * 4.0.0.
	 */
	V4_0("4.0.0"),
	/**
	 * 4.1.0.
	 */
	V4_1("4.1.0"),
	/**
	 * 4.2.0.
	 */
	V4_2("4.2.0-SNAPSHOT");

	private final String version;

	SupportedBootVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return this.version;
	}

	@Override
	public String toString() {
		return getVersion();
	}

	/**
	 * Returns the latest supported Spring Boot version.
	 * @return the latest supported Spring Boot version
	 */
	public static SupportedBootVersion latest() {
		return V4_1;
	}

}
