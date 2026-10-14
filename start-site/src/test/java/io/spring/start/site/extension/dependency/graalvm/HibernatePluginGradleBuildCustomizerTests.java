/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.graalvm;

import io.spring.initializr.generator.buildsystem.gradle.GradleBuild;
import io.spring.initializr.generator.buildsystem.gradle.GradlePlugin;
import io.spring.initializr.generator.buildsystem.gradle.StandardGradlePlugin;
import io.spring.initializr.generator.version.Version;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link HibernatePluginGradleBuildCustomizer}.
 *
 * @author Stephane Nicoll
 */
class HibernatePluginGradleBuildCustomizerTests {

	@Test
	void customizerAppliesHibernateEnhancePlugin() {
		HibernatePluginGradleBuildCustomizer customizer = new HibernatePluginGradleBuildCustomizer(
				Version.parse("6.1.0.Final"));
		GradleBuild build = new GradleBuild();
		customizer.customize(build);
		GradlePlugin hibernatePlugin = build.plugins()
			.values()
			.filter((plugin) -> plugin.getId().equals("org.hibernate.orm"))
			.findAny()
			.orElse(null);
		assertThat(hibernatePlugin).isNotNull();
		assertThat(hibernatePlugin).isInstanceOf(StandardGradlePlugin.class)
			.satisfies((plugin) -> assertThat(((StandardGradlePlugin) plugin).getVersion()).isEqualTo("6.1.0.Final"));
	}

}
