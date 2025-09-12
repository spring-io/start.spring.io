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

package io.spring.start.site.extension.description;

import io.spring.initializr.generator.language.kotlin.KotlinLanguage;
import io.spring.initializr.generator.test.io.TextAssert;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link InvalidJvmVersionHelpDocumentCustomizer}.
 *
 * @author Stephane Nicoll
 */
class InvalidJvmVersionHelpDocumentCustomizerTests extends AbstractExtensionTests {

	@Test
	void warningAddedWithUnsupportedCombination() {
		assertHelpDocument("11").lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed from '11' to '17', review the [JDK Version Range](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-Versions#jdk-version-range) on the wiki for more details.");
	}

	@Test
	void warningAddedWithUnsupportedKotlinVersion() {
		ProjectRequest request = createProjectRequest("web");
		request.setJavaVersion("22");
		request.setLanguage(KotlinLanguage.ID);
		assertHelpDocument(request).lines()
			.containsSubsequence("# Read Me First",
					"* The JVM level was changed from '22' to '21' as the Kotlin version does not support Java 22 yet.");
	}

	@Test
	void warningNotAddedWithCompatibleVersion() {
		assertHelpDocument("17").doesNotContain("# Read Me First");
	}

	private TextAssert assertHelpDocument(ProjectRequest request) {
		return assertThat(helpDocument(request));
	}

	private TextAssert assertHelpDocument(String jvmVersion) {
		ProjectRequest request = createProjectRequest("web");
		request.setType("gradle-project");
		request.setJavaVersion(jvmVersion);
		return assertHelpDocument(request);
	}

}
