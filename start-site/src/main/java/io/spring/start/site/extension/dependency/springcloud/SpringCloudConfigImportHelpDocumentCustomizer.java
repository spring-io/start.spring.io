/*
 * Copyright 2012-2021 the original author or authors.
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

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spring.initializr.generator.io.template.TemplateRenderer;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import io.spring.initializr.generator.spring.documentation.HelpDocumentCustomizer;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;

/**
 * Adds default imports for Spring Cloud Config clients and integrations.
 *
 * @author Olga Maciaszek-Sharma
 */
class SpringCloudConfigImportHelpDocumentCustomizer implements HelpDocumentCustomizer {

	private static final VersionRange SPRING_BOOT_2_4_0_OR_LATER = VersionParser.DEFAULT.parseRange("2.4.0");

	private final ProjectDescription description;

	private final TemplateRenderer templateRenderer;

	SpringCloudConfigImportHelpDocumentCustomizer(ProjectDescription description, TemplateRenderer templateRenderer) {
		this.description = description;
		this.templateRenderer = templateRenderer;
	}

	Set<ConfigSource> getConfigSources() {
		Set<String> buildDependencies = this.description.getRequestedDependencies().keySet();
		Map<String, ConfigSource> configSources = Stream
				.of(new String[][] { { "cloud-config-client", "configserver", "Config Server", "8888" },
						{ "cloud-starter-consul-config", "consul", "Consul", "8500" },
						{ "cloud-starter-zookeeper-config", "zookeeper", "Zookeeper", "2181" },
						{ "cloud-starter-vault-config", "vault", "Vault", "8200" } })
				.collect(Collectors.toMap((data) -> data[0], (data) -> new ConfigSource(data[1], data[2], data[3])));

		return configSources.entrySet().stream()
				.filter((configSourceEntry) -> buildDependencies.contains(configSourceEntry.getKey()))
				.map(Map.Entry::getValue).collect(Collectors.toSet());
	}

	@Override
	public void customize(HelpDocument helpDocument) {
		Version platformVersion = this.description.getPlatformVersion();
		if (SPRING_BOOT_2_4_0_OR_LATER.match((platformVersion))) {
			Set<ConfigSource> configSources = getConfigSources();
			if (!configSources.isEmpty()) {
				helpDocument.gettingStarted().addReferenceDocLink(
						"https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-files",
						"Spring Boot External Application Properties");
				helpDocument.addSection(new SpringBootConfigImportSection(this.templateRenderer, configSources));
			}
		}
	}

	protected static class ConfigSource {

		String key;

		String name;

		String defaultPort;

		ConfigSource(String key, String name, String defaultPort) {
			this.key = key;
			this.name = name;
			this.defaultPort = defaultPort;
		}

	}

}
