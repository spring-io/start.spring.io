/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import java.util.Map;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.versionresolver.MavenVersionResolver;

/**
 * Resolve the Native Build Tools version to use based on the platform version.
 *
 * @author Stephane Nicoll
 */
abstract class NativeBuildtoolsVersionResolver {

	static String resolve(MavenVersionResolver versionResolver, Version platformVersion) {
		Map<String, String> resolve = versionResolver.resolvePlugins("org.springframework.boot",
				"spring-boot-dependencies", platformVersion.toString());
		return resolve.get("org.graalvm.buildtools:native-maven-plugin");
	}

}
