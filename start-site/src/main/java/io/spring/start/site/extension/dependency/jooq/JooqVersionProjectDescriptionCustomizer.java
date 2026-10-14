/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.jooq;

import java.util.Set;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.project.ProjectDescriptionField;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionParser;
import io.spring.initializr.generator.version.VersionRange;
import io.spring.start.site.project.JavaVersionProjectDescriptionCustomizer;

/**
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 * <p>
 * jOOQ 3.20+ requires Java 21.
 *
 * @author Moritz Halbritter
 */
public class JooqVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

	private static final VersionRange SPRING_BOOT_4_1_OR_LATER = VersionParser.DEFAULT.parseRange("4.1.0-M1");

	@Override
	public int getOrder() {
		return JavaVersionProjectDescriptionCustomizer.ORDER + 10;
	}

	@Override
	public void customize(MutableProjectDescription description) {
		Set<String> dependencyIds = description.getRequestedDependencies().keySet();
		if (!dependencyIds.contains("jooq")) {
			return;
		}
		Version bootVersion = description.getPlatformVersion();
		if (SPRING_BOOT_4_1_OR_LATER.match(bootVersion)) {
			Language language = description.getLanguage();
			Integer javaGeneration = determineJavaGeneration(language.jvmVersion());
			if (javaGeneration != null && javaGeneration < 21) {
				Language compatibleLanguage = Language.forId(description.getLanguage().id(), "21");
				description.setLanguage(compatibleLanguage);
				description.getChanges()
					.add(ProjectDescriptionField.JVM_VERSION,
							"The JVM level was changed to '21' as jOOQ requires Java 21 or later.");
			}
		}
	}

	private Integer determineJavaGeneration(String javaVersion) {
		try {
			return Integer.parseInt(javaVersion);
		}
		catch (NumberFormatException ex) {
			return null;
		}
	}

}
