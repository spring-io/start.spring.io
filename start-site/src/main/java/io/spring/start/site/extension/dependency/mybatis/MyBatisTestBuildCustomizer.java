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

package io.spring.start.site.extension.dependency.mybatis;

import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.buildsystem.DependencyScope;
import io.spring.initializr.generator.spring.build.BuildCustomizer;

/**
 * A {@link BuildCustomizer} that automatically adds {@code mybatis-spring-boot-starter-test} when
 * MyBatis is selected.
 *
 * @author Kazuki Shimizu
 */
public class MyBatisTestBuildCustomizer implements BuildCustomizer<Build> {

	@Override
	public void customize(Build build) {
		Dependency mybatis = build.dependencies().get("mybatis");
		build.dependencies()
			.add("mybatis-test", Dependency.withCoordinates(mybatis.getGroupId(), mybatis.getArtifactId() + "-test")
				.version(mybatis.getVersion())
				.scope(DependencyScope.TEST_COMPILE));
	}

}
