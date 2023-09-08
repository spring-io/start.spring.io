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

package io.spring.start.site.container;

import java.util.function.Consumer;

import io.spring.initializr.generator.container.docker.compose.ComposeService.Builder;

/**
 * Description of a Docker service.
 *
 * @author Stephane Nicoll
 * @author Chris Bono
 */
public class DockerService implements Consumer<Builder> {

	private final String image;

	private final String imageTag;

	private final String website;

	private final String command;

	private final int[] ports;

	DockerService(String image, String imageTag, String website, String command, int... ports) {
		this.image = image;
		this.imageTag = imageTag;
		this.website = website;
		this.command = command;
		this.ports = ports;
	}

	/**
	 * Return the image identifier of the service.
	 * @return the image
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * Return the image tag of the service.
	 * @return the tag
	 */
	public String getImageTag() {
		return this.imageTag;
	}

	/**
	 * Return the website to use to get more information about available options for the
	 * service.
	 * @return the website
	 */
	public String getWebsite() {
		return this.website;
	}

	/**
	 * Return the command to use to override the default command (optional).
	 * @return the command
	 */
	public String getCommand() {
		return this.command;
	}

	/**
	 * Return the ports that should be exposed by the service.
	 * @return the ports to expose
	 */
	public int[] getPorts() {
		return this.ports;
	}

	@Override
	public void accept(Builder builder) {
		builder.image(this.image)
			.imageTag(this.imageTag)
			.imageWebsite(this.website)
			.ports(this.ports)
			.command(this.command);
	}

}
