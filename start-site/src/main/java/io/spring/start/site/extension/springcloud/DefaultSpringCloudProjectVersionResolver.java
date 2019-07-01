/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.DependencyManagementVersionResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the {@link SpringCloudProjectVersionResolver} that resolves the
 * version for a Spring Cloud project from the release train version that corresponds to
 * the given Spring Boot version.
 *
 * @author Olga Maciaszek-Sharma
 */
class DefaultSpringCloudProjectVersionResolver implements SpringCloudProjectVersionResolver {

	private static final String SPRING_CLOUD_GROUP_ID = "org.springframework.cloud";

	private static final String SPRING_CLOUD_DEPENDENCIES_ID = "spring-cloud-dependencies";

	private static final Log LOG = LogFactory.getLog(DefaultSpringCloudProjectVersionResolver.class);

	private static final String SPRING_CLOUD_ID = "spring-cloud";

	private final InitializrMetadata metadata;

	private final DependencyManagementVersionResolver resolver;

	DefaultSpringCloudProjectVersionResolver(InitializrMetadata metadata,
			DependencyManagementVersionResolver resolver) {
		this.metadata = metadata;
		this.resolver = resolver;
	}

	@Override
	public String resolveVersion(Version bootVersion, String artifactId) {
		BillOfMaterials bom = this.metadata.getConfiguration().getEnv().getBoms().get(SPRING_CLOUD_ID);
		String releaseTrainVersion = bom.resolve(bootVersion).getVersion();
		LOG.info("Retrieving version for artifact: " + artifactId + " and release train version: "
				+ releaseTrainVersion);
		String resolvedProjectVersion = this.resolver
				.resolve(SPRING_CLOUD_GROUP_ID, SPRING_CLOUD_DEPENDENCIES_ID, releaseTrainVersion).get(artifactId);
		return resolvedProjectVersion;
	}

}
