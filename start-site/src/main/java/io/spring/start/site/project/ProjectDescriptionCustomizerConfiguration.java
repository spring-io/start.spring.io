/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.project;

import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.start.site.extension.dependency.jooq.JooqVersionProjectDescriptionCustomizer;
import io.spring.start.site.extension.dependency.timefold.TimefoldVersionProjectDescriptionCustomizer;
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

	@Bean
	public JooqVersionProjectDescriptionCustomizer jooqVersionProjectDescriptionCustomizer() {
		return new JooqVersionProjectDescriptionCustomizer();
	}

	@Bean
	TimefoldVersionProjectDescriptionCustomizer timefoldVersionProjectDescriptionCustomizer() {
		return new TimefoldVersionProjectDescriptionCustomizer();
	}

}
