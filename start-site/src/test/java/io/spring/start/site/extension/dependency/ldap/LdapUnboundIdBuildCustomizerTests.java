/*
 * Copyright 2012 - present the original author or authors.
 */

package io.spring.start.site.extension.dependency.ldap;

import io.spring.initializr.web.project.ProjectRequest;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link LdapUnboundIdBuildCustomizer}.
 *
 * @author Moritz Halbritter
 */
class LdapUnboundIdBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void shouldAddUnbound() {
		ProjectRequest request = createProjectRequest("unboundid-ldap");
		assertThat(mavenPom(request))
			.hasDependency("org.springframework.boot", "spring-boot-starter-ldap", null, "test")
			.hasDependency("com.unboundid", "unboundid-ldapsdk", null, "test");
	}

	@Test
	void shouldNotAddLdapStarterTwiceWhenLdapIsSelected() {
		ProjectRequest request = createProjectRequest("ldap", "unboundid-ldap");
		assertThat(mavenPom(request)).hasDependency("org.springframework.boot", "spring-boot-starter-ldap")
			.hasDependency("org.springframework.boot", "spring-boot-starter-ldap-test", null, "test")
			.hasDependency("com.unboundid", "unboundid-ldapsdk", null, "test");
	}

}
