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

/**
 * Supported Spring Boot versions.
 *
 * @author Moritz Halbritter
 */
public enum SupportedBootVersion {

	/**
	 * 3.5.0.
	 */
	V3_5("3.5.0"),
	/**
	 * 4.0.0.
	 */
	V4_0("4.0.0");

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
		return V4_0;
	}

}
