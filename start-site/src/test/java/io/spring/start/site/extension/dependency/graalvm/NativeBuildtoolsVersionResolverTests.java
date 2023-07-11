/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.graalvm;

import java.nio.file.Path;
import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.metadata.MetadataElement;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NativeBuildtoolsVersionResolver}.
 *
 * @author Stephane Nicoll
 */
@TestInstance(Lifecycle.PER_CLASS)
class NativeBuildtoolsVersionResolverTests extends AbstractExtensionTests {

	private MavenVersionResolver versionResolver;

	@BeforeEach
	void createVersionResolver(@TempDir Path temp) {
		this.versionResolver = MavenVersionResolver.withCacheLocation(temp);
	}

	@ParameterizedTest
	@MethodSource("platformVersions")
	void resolveNativeBuildToolsVersion(Version platformVersion) {
		assertThat(NativeBuildtoolsVersionResolver.resolve(this.versionResolver, platformVersion)).isNotNull();
	}

	private Stream<Arguments> platformVersions() {
		return getMetadata().getBootVersions()
			.getContent()
			.stream()
			.map(MetadataElement::getId)
			.filter((candidate) -> !candidate.startsWith("2.7"))
			.map(this::version);
	}

	private Arguments version(String platformVersion) {
		return Arguments.of(VersionParser.DEFAULT.parse(platformVersion));
	}

}
