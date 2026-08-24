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

package io.spring.start.site.extension.dependency.springcloud;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Resolve Spring Cloud artifact versions using a {@link MavenVersionResolver}.
 *
 * @author Olga Maciaszek-Sharma
 * @author Stephane Nicoll
 */
class SpringCloudProjectVersionResolver {

	private static final Log logger = LogFactory.getLog(SpringCloudProjectVersionResolver.class);

	private final InitializrMetadata metadata;

	private final MavenVersionResolver versionResolver;

	SpringCloudProjectVersionResolver(InitializrMetadata metadata, MavenVersionResolver versionResolver) {
		this.metadata = metadata;
		this.versionResolver = versionResolver;
	}

	/**
	 * Resolve the version of a specified artifact that matches the provided Spring Boot
	 * version, using the {@code spring-cloud} bom.
	 * @param platformVersion the Spring Boot version to check the Spring Cloud Release
	 * train version against
	 * @param dependencyId the dependency id of the Spring Cloud artifact in the form of
	 * {@code groupId:artifactId}
	 * @return the appropriate project version or {@code null} if the resolution failed
	 */
	String resolveVersion(Version platformVersion, String dependencyId) {
		return resolveVersion("spring-cloud", platformVersion, dependencyId);
	}

	/**
	 * Resolve the version of a specified artifact that matches the provided Spring Boot
	 * version, using the bom registered under the given name.
	 * @param bomName the name of the bom, as registered in the initializr metadata
	 * @param platformVersion the Spring Boot version to check the bom's version against
	 * @param dependencyId the dependency id of the artifact managed by the bom, in the
	 * form of {@code groupId:artifactId}
	 * @return the appropriate project version or {@code null} if the resolution failed
	 */
	String resolveVersion(String bomName, Version platformVersion, String dependencyId) {
		BillOfMaterials bom = this.metadata.getConfiguration().getEnv().getBoms().get(bomName);
		if (bom == null) {
			return null;
		}
		String bomVersion = bom.resolve(platformVersion).getVersion();
		logger.info("Retrieving version for artifact: " + dependencyId + " and bom version: " + bomVersion);
		return this.versionResolver.resolveDependencies(bom.getGroupId(), bom.getArtifactId(), bomVersion)
			.get(dependencyId);
	}

}
