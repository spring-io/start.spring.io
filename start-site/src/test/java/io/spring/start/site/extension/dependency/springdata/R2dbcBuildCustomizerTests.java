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

package io.spring.start.site.extension.dependency.springdata;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
import io.spring.initializr.generator.version.Version;
import io.spring.initializr.generator.version.VersionReference;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.support.MetadataBuildItemResolver;
import io.spring.start.site.extension.AbstractExtensionTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link R2dbcBuildCustomizer}.
 *
 * @author Stephane Nicoll
 * @author Eddú Meléndez
 */
class R2dbcBuildCustomizerTests extends AbstractExtensionTests {

	@Test
	void r2dbcWithH2() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("h2");
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "h2", "r2dbc-h2");
	}

	@Test
	void r2dbcWithMariadbAndBorca() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("mariadb");
		customize(build, Version.parse("2.7.6"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "mariadb", "r2dbc-mariadb");
		assertThat(build.dependencies().get("r2dbc-mariadb").getVersion()).isNull();
	}

	@Test
	void r2dbcWithMariadbAfterBorca() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("mariadb");
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "mariadb", "r2dbc-mariadb");
		assertThat(build.dependencies().get("r2dbc-mariadb").getVersion()).isEqualTo(VersionReference.ofValue("1.1.3"));
	}

	@Test
	void r2dbcWithMysql() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("mysql");
		customize(build, Version.parse("2.6.8"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "mysql", "r2dbc-mysql");
		assertThat(build.dependencies().get("r2dbc-mysql").getGroupId()).isEqualTo("dev.miku");
	}

	@Test
	void r2dbcWithMysqlAndBorca() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("mysql");
		customize(build, Version.parse("2.7.0"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "mysql");
	}

	@Test
	void r2dbcWithMysqlAndSpringBoot31() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("mysql");
		customize(build, Version.parse("3.1.0"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "mysql", "r2dbc-mysql");
		assertThat(build.dependencies().get("r2dbc-mysql").getGroupId()).isEqualTo("io.asyncer");
	}

	@Test
	void r2dbcWithPostgresql() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("postgresql");
		customize(build, Version.parse("2.6.8"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "postgresql", "r2dbc-postgresql");
		assertThat(build.dependencies().get("r2dbc-postgresql").getGroupId()).isEqualTo("io.r2dbc");
	}

	@Test
	void r2dbcWithPostgresqlAndBorca() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("postgresql");
		customize(build, Version.parse("3.0.0-M2"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "postgresql", "r2dbc-postgresql");
		assertThat(build.dependencies().get("r2dbc-postgresql").getGroupId()).isEqualTo("org.postgresql");
	}

	@Test
	void r2dbcWithSqlserver() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("sqlserver");
		customize(build, Version.parse("2.7.6"));
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "sqlserver", "r2dbc-mssql");
		assertThat(build.dependencies().get("r2dbc-mssql").getVersion()).isNull();
	}

	@Test
	void r2dbcWithSqlserverAfterBorca() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("sqlserver");
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "sqlserver", "r2dbc-mssql");
		assertThat(build.dependencies().get("r2dbc-mssql").getVersion())
			.isEqualTo(VersionReference.ofValue("1.0.0.RELEASE"));
	}

	@Test
	void r2dbcWithOracleAfterBorca() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("oracle");
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "oracle", "oracle-r2dbc");
	}

	@Test
	void r2dbcWithFlywayAddSpringJdbc() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("flyway");
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "flyway", "spring-jdbc");
	}

	@ParameterizedTest
	@ValueSource(strings = { "jdbc", "data-jdbc", "data-jpa" })
	void r2dbcWithFlywayAndJdbcStaterDoesNotAddSpringJdbc(String jdbcStarter) {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("flyway");
		build.dependencies().add(jdbcStarter);
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "flyway", jdbcStarter);
	}

	@Test
	void r2dbcWithLiquibaseAddSpringJdbc() {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("liquibase");
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "liquibase", "spring-jdbc");
	}

	@ParameterizedTest
	@ValueSource(strings = { "jdbc", "data-jdbc", "data-jpa" })
	void r2dbcWithLiquibaseAndJdbcStaterDoesNotAddSpringJdbc(String jdbcStarter) {
		Build build = createBuild();
		build.dependencies().add("data-r2dbc");
		build.dependencies().add("liquibase");
		build.dependencies().add(jdbcStarter);
		customize(build);
		assertThat(build.dependencies().ids()).containsOnly("data-r2dbc", "liquibase", jdbcStarter);
	}

	private Build createBuild() {
		InitializrMetadata metadata = getMetadata();
		return new MavenBuild(new MetadataBuildItemResolver(metadata, getDefaultPlatformVersion(metadata)));
	}

	private void customize(Build build) {
		customize(build, getDefaultPlatformVersion(getMetadata()));
	}

	private void customize(Build build, Version platformVersion) {
		new R2dbcBuildCustomizer(platformVersion).customize(build);
	}

	private Version getDefaultPlatformVersion(InitializrMetadata metadata) {
		return Version.parse(metadata.getBootVersions().getDefault().getId());
	}

}
