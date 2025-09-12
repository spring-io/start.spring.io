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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.util.FileSystemUtils;

/**
 * Utility to work with temporary files.
 *
 * @author Moritz Halbritter
 */
public final class TemporaryFiles {

	private static final AtomicBoolean CLEANUP_REGISTERED = new AtomicBoolean();

	private static final Set<Path> CLEANUP_PATHS = ConcurrentHashMap.newKeySet();

	private TemporaryFiles() {
	}

	public static Path newTemporaryDirectory(String prefix) throws IOException {
		String name = prefix + System.nanoTime();
		Path path = getTempDir().resolve(name);
		Files.createDirectories(path);
		registerCleanup();
		CLEANUP_PATHS.add(path);
		return path;
	}

	private static void registerCleanup() {
		if (CLEANUP_REGISTERED.compareAndSet(false, true)) {
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				for (Path cleanupPath : CLEANUP_PATHS) {
					try {
						FileSystemUtils.deleteRecursively(cleanupPath);
					}
					catch (IOException ex) {
						// Ignore
					}
				}
			}));
		}
	}

	public static Path getTempDir() {
		String property = System.getenv("START_SPRING_IO_TMPDIR");
		if (property != null) {
			return Path.of(property);
		}
		property = System.getProperty("java.io.tmpdir");
		if (property != null) {
			return Path.of(property);
		}
		throw new IllegalStateException(
				"Unable to find temporary directory. Please set either the env variable START_SPRING_IO_TMPDIR, or the system property java.io.tmpdir");
	}

}
