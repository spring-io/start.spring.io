/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.springsecurity;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SpringSecurityLdapBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class SpringSecurityLdapBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void securityLdapIsAddedWithSecurityAndLdap() {
		ProjectRequest request = createProjectRequest("security", "ldap");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("security"))
			.hasDependency(Dependency.createSpringBootStarter("ldap"))
			.hasDependency("org.springframework.security", "spring-security-ldap");
	}

	@Test
	void securityLdapIsNotAddedWithLdapOnly() {
		ProjectRequest request = createProjectRequest("ldap");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("ldap"))
			.doesNotHaveDependency("org.springframework.security", "spring-security-ldap");
	}

	@Test
	void securityLdapIsNotAddedWithSecurityOnly() {
		ProjectRequest request = createProjectRequest("security");
		assertThat(mavenPom(request)).hasDependency(Dependency.createSpringBootStarter("security"))
			.doesNotHaveDependency("org.springframework.security", "spring-security-ldap");
	}

}
