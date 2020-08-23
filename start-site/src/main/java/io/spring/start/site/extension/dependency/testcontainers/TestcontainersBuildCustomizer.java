/*
 * Copyright 2012-2020 the original author or authors.
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

package io.spring.start.site.extension.dependency.testcontainers;

import java.util.List;
import java.util.stream.Collectors;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InvalidInitializrMetadataException;

/**
 * {@link BuildCustomizer} for Testcontainers that add a module-specific module if a
 * supported entry is selected.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 */
class TestcontainersBuildCustomizer implements BuildCustomizer<Build> {

	private static final String BOM_ID = "testcontainers";

	private final TestContainerModuleRegistry modulesRegistry;

	private final BillOfMaterials testContainersBom;

	TestcontainersBuildCustomizer(TestContainerModuleRegistry modulesRegistry, InitializrMetadata metadata,
			ProjectDescription description) {
		this.modulesRegistry = modulesRegistry;
		this.testContainersBom = resolveBom(metadata, description);
	}

	private static BillOfMaterials resolveBom(InitializrMetadata metadata, ProjectDescription description) {
		try {
			return metadata.getConfiguration().getEnv().getBoms().get(BOM_ID).resolve(description.getPlatformVersion());
		}
		catch (InvalidInitializrMetadataException ex) {
			return null;
		}
	}

	@Override
	public void customize(Build build) {
		this.modulesRegistry.modules().forEach((module) -> module.customize(build));
		List<String> testContainerDependencies = build.dependencies().ids()
				.filter((id) -> id.startsWith("testcontainers")).collect(Collectors.toList());
		if (this.testContainersBom != null && testContainerDependencies.size() > 1) {
			build.dependencies().remove("testcontainers");
			build.properties().version(this.testContainersBom.getVersionProperty(),
					this.testContainersBom.getVersion());
			build.boms().add(BOM_ID);
		}
	}

}
