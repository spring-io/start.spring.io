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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.spring.initializr.generator.language.Language;
import io.spring.initializr.generator.project.MutableProjectDescription;
import io.spring.initializr.generator.project.ProjectDescriptionCustomizer;
import io.spring.initializr.generator.version.Version;

/**
 * Validate that the requested java version is compatible with the chosen Spring Boot
 * generation and adapt the request if necessary.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Moritz Halbritter
 * @author Mukesh Lilawat
 */
public class JavaVersionProjectDescriptionCustomizer implements ProjectDescriptionCustomizer {

    private static final List<String> UNSUPPORTED_VERSIONS = Arrays.asList("1.6", "1.7", "1.8");

    private static final int MAX_JAVA_VERSION = 25;

    private final JavaVersionMapping mapping = new JavaVersionMapping();

    @Override
    public void customize(MutableProjectDescription description) {
        if (description == null || description.getLanguage() == null) {
            // Defensive: Avoid null pointer situations
            return;
        }

        Language language = description.getLanguage();
        Version bootVersion = description.getPlatformVersion();
        String javaVersion = language.jvmVersion();

        if (javaVersion == null || javaVersion.isBlank()) {
            // Default fallback if JVM version not specified
            updateTo(description, mapping.getMinJavaVersion(bootVersion, language));
            return;
        }

        int minSupported = mapping.getMinJavaVersion(bootVersion, language);
        int maxSupported = mapping.getMaxJavaVersion(bootVersion, language);

        // 1️⃣ Handle unsupported legacy versions (Java 6,7,8)
        if (UNSUPPORTED_VERSIONS.contains(javaVersion)) {
            updateTo(description, minSupported);
            return;
        }

        // 2️⃣ Determine actual Java generation
        Integer javaGeneration = determineJavaGeneration(javaVersion);

        if (javaGeneration == null) {
            // If unable to parse version, fallback to min supported
            updateTo(description, minSupported);
            return;
        }

        // 3️⃣ Validate version boundaries
        if (javaGeneration < minSupported) {
            updateTo(description, minSupported);
            return;
        }

        if (javaGeneration > maxSupported) {
            updateTo(description, maxSupported);
            return;
        }

        // 4️⃣ If within valid range, ensure consistency
        if (!Objects.equals(javaGeneration, Integer.valueOf(javaVersion))) {
            updateTo(description, javaGeneration);
        }
    }

    private void updateTo(MutableProjectDescription description, int jvmVersion) {
        if (description == null || description.getLanguage() == null) {
            return;
        }

        Language currentLang = description.getLanguage();
        Language updatedLang = Language.forId(currentLang.id(), Integer.toString(jvmVersion));

        // Only update if different (avoid unnecessary reset)
        if (!currentLang.jvmVersion().equals(updatedLang.jvmVersion())) {
            description.setLanguage(updatedLang);
        }
    }

    private Integer determineJavaGeneration(String javaVersion) {
        try {
            int generation = Integer.parseInt(javaVersion.trim());
            // Enforce reasonable version boundaries
            if (generation >= 9 && generation <= MAX_JAVA_VERSION) {
                return generation;
            }
        }
        catch (NumberFormatException ex) {
            // Log ignored invalid versions gracefully
            System.err.println("[WARN] Invalid Java version format: " + javaVersion);
        }
        return null;
    }
}
