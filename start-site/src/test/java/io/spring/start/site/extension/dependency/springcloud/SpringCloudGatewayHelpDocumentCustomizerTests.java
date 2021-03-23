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

import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.io.template.MustacheTemplateRenderer;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionDiff;
import io.spring.initializr.generator.spring.documentation.HelpDocument;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link SpringCloudGatewayHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class SpringCloudGatewayHelpDocumentCustomizerTests {

	@Test
	void originalWithSpringCloudGatewayAndSpringWebAddsWarning() {
		HelpDocument document = customizeEmptyDocument(createDiff("web", "cloud-gateway"));
		assertThat(document.getWarnings().getItems()).hasSize(1);
		assertThat(document.getWarnings().getItems().get(0)).isEqualTo(
				"Spring Cloud Gateway requires Spring WebFlux, your choice of Spring Web has been replaced accordingly.");
	}

	@Test
	void originalWithSpringCloudGatewayDoesNotAddWarning() {
		HelpDocument document = customizeEmptyDocument(createDiff("cloud-gateway"));
		assertThat(document.getWarnings().getItems()).isEmpty();
	}

	@Test
	void originalWithSpringWebOnlyDoesNotAddWarning() {
		HelpDocument document = customizeEmptyDocument(createDiff("web"));
		assertThat(document.getWarnings().getItems()).isEmpty();
	}

	private HelpDocument customizeEmptyDocument(ProjectDescriptionDiff diff) {
		HelpDocument document = new HelpDocument(mock(MustacheTemplateRenderer.class));
		new SpringCloudGatewayHelpDocumentCustomizer(diff).customize(document);
		return document;
	}

	private ProjectDescriptionDiff createDiff(String... dependencies) {
		MutableProjectDescription description = new MutableProjectDescription();
		for (String dependency : dependencies) {
			description.addDependency(dependency, mock(Dependency.class));
		}
		return new ProjectDescriptionDiff(description);
	}

}
