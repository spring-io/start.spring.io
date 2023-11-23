/*
 * Copyright 2012-2023 the original author or authors.
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

package io.spring.start.site.extension.dependency.graalvm;

import java.util.Map;
import java.util.function.Supplier;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnRequestedDependency;
import io.spring.initializr.generator.condition.ProjectGenerationCondition;
import io.spring.initializr.generator.language.groovy.GroovyLanguage;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.versionresolver.MavenVersionResolver;
import io.spring.start.site.extension.dependency.graalvm.GraalVmProjectGenerationConfiguration.CompatibleLanguageCondition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.function.SingletonSupplier;

/**
 * {@link ProjectGenerationConfiguration} for generation of projects that use GraalVM.
 *
 * @author Stephane Nicoll
 */
@ProjectGenerationConfiguration
@ConditionalOnRequestedDependency("native")
@Conditional(CompatibleLanguageCondition.class)
class GraalVmProjectGenerationConfiguration {

	private final Supplier<String> nbtVersion;

	GraalVmProjectGenerationConfiguration(ProjectDescription description, MavenVersionResolver versionResolver) {
		this.nbtVersion = SingletonSupplier
			.of(() -> NativeBuildtoolsVersionResolver.resolve(versionResolver, description.getPlatformVersion()));
	}

	@Bean
	GraalVmBuildCustomizer graalVmBuildCustomizer() {
		return new GraalVmBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(MavenBuildSystem.ID)
	GraalVmMavenBuildCustomizer graalVmMavenBuildCustomizer() {
		return new GraalVmMavenBuildCustomizer();
	}

	@Bean
	@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
	GraalVmGroovyDslGradleBuildCustomizer graalVmGroovyDslGradleBuildCustomizer() {
		return new GraalVmGroovyDslGradleBuildCustomizer(this.nbtVersion.get());
	}

	@Bean
	@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
	GraalVmKotlinDslGradleBuildCustomizer graalVmKotlinDslGradleBuildCustomizer() {
		return new GraalVmKotlinDslGradleBuildCustomizer(this.nbtVersion.get());
	}

	@Bean
	GraalVmHelpDocumentCustomizer graalVmHelpDocumentCustomizer(InitializrMetadata metadata,
			ProjectDescription description, Build build) {
		return new GraalVmHelpDocumentCustomizer(metadata, description, build);
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnRequestedDependency("data-jpa")
	static class HibernateConfiguration {

		private final Version platformVersion;

		HibernateConfiguration(ProjectDescription description) {
			this.platformVersion = description.getPlatformVersion();
		}

		@Bean
		@ConditionalOnBuildSystem(MavenBuildSystem.ID)
		HibernatePluginMavenBuildCustomizer hibernatePluginMavenBuildCustomizer() {
			return new HibernatePluginMavenBuildCustomizer();
		}

		@Bean
		@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_GROOVY)
		HibernatePluginGroovyDslGradleBuildCustomizer hibernatePluginGroovyDslGradleBuildCustomizer(
				MavenVersionResolver versionResolver) {
			return new HibernatePluginGroovyDslGradleBuildCustomizer(determineHibernateVersion(versionResolver));
		}

		@Bean
		@ConditionalOnBuildSystem(value = GradleBuildSystem.ID, dialect = GradleBuildSystem.DIALECT_KOTLIN)
		HibernatePluginKotlinDslGradleBuildCustomizer hibernatePluginKotlinDslGradleBuildCustomizer(
				MavenVersionResolver versionResolver) {
			return new HibernatePluginKotlinDslGradleBuildCustomizer(determineHibernateVersion(versionResolver));
		}

		private Version determineHibernateVersion(MavenVersionResolver versionResolver) {
			Map<String, String> resolve = versionResolver.resolveDependencies("org.springframework.boot",
					"spring-boot-dependencies", this.platformVersion.toString());
			String hibernateVersion = resolve.get("org.hibernate.orm" + ":hibernate-core");
			if (hibernateVersion == null) {
				throw new IllegalStateException(
						"Failed to determine Hibernate version for Spring Boot " + this.platformVersion);
			}
			return Version.parse(hibernateVersion);
		}

	}

	/**
	 * A {@link ProjectGenerationCondition} that match for any language that isn't Groovy.
	 */
	static class CompatibleLanguageCondition extends ProjectGenerationCondition {

		@Override
		protected boolean matches(ProjectDescription description, ConditionContext context,
				AnnotatedTypeMetadata metadata) {
			return !GroovyLanguage.ID.equals(description.getLanguage().id());
		}

	}

}
