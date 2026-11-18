/*
 * Copyright and licensing information for this source code can be found in
 * the NOTICE and LICENSE files distributed with this project.
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
