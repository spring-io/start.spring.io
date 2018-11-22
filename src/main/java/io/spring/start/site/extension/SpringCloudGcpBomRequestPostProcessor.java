/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.util.Version;

import org.springframework.stereotype.Component;

/**
 * Add the Spring Cloud GCP BOM for older releases now that the project has joined the
 * official release train.
 *
 * @author Stephane Nicoll
 */
@Component
class SpringCloudGcpBomRequestPostProcessor
		extends AbstractProjectRequestPostProcessor {

	private static final Version VERSION_2_1_0_M1 = Version.parse("2.1.0.M1");

	public static final BillOfMaterials SPRING_CLOUD_GCP_BOM = BillOfMaterials.create(
			"org.springframework.cloud", "spring-cloud-gcp-dependencies",
			"1.0.0.RELEASE");

	@Override
	public void postProcessAfterResolution(ProjectRequest request,
			InitializrMetadata metadata) {
		if (isSpringBootVersionBefore(request, VERSION_2_1_0_M1)
				&& request.getResolvedDependencies().stream().anyMatch(
						(dependency) -> dependency.getId().startsWith("cloud-gcp"))) {
			request.getBoms().put("spring-cloud-gcp", SPRING_CLOUD_GCP_BOM);
		}
	}

}
