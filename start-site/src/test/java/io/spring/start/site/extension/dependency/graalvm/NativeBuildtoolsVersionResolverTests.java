/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.metadata.MetadataElement;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NativeBuildtoolsVersionResolver}.
 *
 * @author Stephane Nicoll
 */
@TestInstance(Lifecycle.PER_CLASS)
class NativeBuildtoolsVersionResolverTests extends AbstractExtensionTests {

	@ParameterizedTest
	@MethodSource("platformVersions")
	void resolveNativeBuildToolsVersion(Version platformVersion, @Autowired MavenVersionResolver versionResolver) {
		assertThat(NativeBuildtoolsVersionResolver.resolve(versionResolver, platformVersion)).isNotNull();
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
