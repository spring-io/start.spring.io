/*
 * Copyright 2012-2019 the original author or authors.
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

package io.spring.start.site.extension.springcloud;

import java.util.Map;

/**
 * Spring Cloud projects metadata for given release train version.
 *
 * @author Olga Maciaszek-Sharma
 */
public class SpringCloudProjectsMetadata {

	private final String releaseTrainVersion;

	private final Map<String, String> projects;

	SpringCloudProjectsMetadata(String releaseTrainVersion,
			Map<String, String> projects) {
		this.releaseTrainVersion = releaseTrainVersion;
		this.projects = projects;
	}

	String getReleaseTrainVersion() {
		return this.releaseTrainVersion;
	}

	Map<String, String> getProjects() {
		return this.projects;
	}

}
