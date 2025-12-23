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

package io.spring.start.site.project;

import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.start.site.extension.dependency.vaadin.VaadinVersionProjectDescriptionCustomizer;
import io.spring.start.site.project.dependency.springcloud.SpringCloudResilience4JProjectDescriptionCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for {@link ProjectDescriptionCustomizer}s.
 *
 * @author Madhura Bhave
 */
@Configuration
public class ProjectDescriptionCustomizerConfiguration {

	@Bean
	public JavaVersionProjectDescriptionCustomizer javaVersionProjectDescriptionCustomizer() {
		return new JavaVersionProjectDescriptionCustomizer();
	}

	@Bean
	public SpringCloudResilience4JProjectDescriptionCustomizer springCloudResilience4JProjectDescriptionCustomizer() {
		return new SpringCloudResilience4JProjectDescriptionCustomizer();
	}

	@Bean
	public VaadinVersionProjectDescriptionCustomizer vaadinVersionProjectDescriptionCustomizer() {
		return new VaadinVersionProjectDescriptionCustomizer();
	}

}
