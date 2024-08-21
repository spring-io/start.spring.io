/*
 * Copyright 2012-2024 the original author or authors.
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
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

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

	private final Set<Path> homes = ConcurrentHashMap.newKeySet();

	private final Queue<Path> freeHomes = new ConcurrentLinkedQueue<>();

	private final AtomicInteger counter = new AtomicInteger();

	private final String prefix;

	public Homes(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Acquires a path to the home. Callers are responsible to call {@link #release(Path)}
	 * when done.
	 * @return the path to the home
	 */
	public Path acquire() {
		Path home = this.freeHomes.poll();
		if (home == null) {
			home = createTempDirectory();
			this.homes.add(home);
		}
		return home;
	}

	/**
	 * Releases a path to the home.
	 * @param home the path to the home
	 */
	public void release(Path home) {
		Assert.state(this.homes.contains(home), "Invalid home '%s'".formatted(home));
		this.freeHomes.add(home);
	}

	private Path createTempDirectory() {
		try {
			Path path = TemporaryFiles.getTempDir()
				.resolve("homes")
				.resolve(this.prefix + "-" + this.counter.getAndIncrement());
			Files.createDirectories(path);
			return path;
		}
		catch (IOException ex) {
			throw new UncheckedIOException("Failed to create temp directory", ex);
		}
	}

}
