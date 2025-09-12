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

package io.spring.start.testsupport;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages Maven and Gradle home directories.
 * <p>
 * The directories are reused, if possible. If no home is available for reuse, a new one
 * will be created.
 *
 * @author Moritz Halbritter
 */
public class Homes {

	/**
	 * Maven homes.
	 */
	public static final Homes MAVEN = new Homes("maven-home");

	/**
	 * Gradle homes.
	 */
	public static final Homes GRADLE = new Homes("gradle-home");

	private final Path path;

	Homes(String name) {
		this.path = createTempDirectory(name);
	}

	/**
	 * Returns the path to the home.
	 * @return the path to the home
	 */
	public Path get() {
		return this.path;
	}

	private static Path createTempDirectory(String name) {
		try {
			Path path = TemporaryFiles.getTempDir().resolve("homes").resolve(name);
			Files.createDirectories(path);
			return path;
		}
		catch (IOException ex) {
			throw new UncheckedIOException("Failed to create temp directory", ex);
		}
	}

}
