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

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import io.spring.initializr.generator.container.docker.compose.ComposeService;

/**
 * Description of a Docker service.
 *
 * @author Stephane Nicoll
 * @author Chris Bono
 */
public final class DockerService implements Consumer<ComposeService.Builder> {

	private final String image;

	private final String imageTag;

	private final String website;

	private final String command;

	private final int[] ports;

	private DockerService(Builder builder) {
		this.image = builder.image;
		this.imageTag = builder.imageTag;
		this.website = builder.website;
		this.command = builder.command;
		this.ports = builder.ports.stream().mapToInt(Integer::intValue).toArray();
	}

	/**
	 * Create a new builder using the specified image and optional tag.
	 * @param imageAndTag the image (and optional tag) to use for the service
	 * @return the new builder instance.
	 */
	public static DockerService.Builder withImageAndTag(String imageAndTag) {
		return new DockerService.Builder(imageAndTag);
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
	 * Return the command to use, if any.
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
	public void accept(ComposeService.Builder builder) {
		builder.image(this.image)
			.imageTag(this.imageTag)
			.imageWebsite(this.website)
			.command(this.command)
			.ports(this.ports);
	}

	/**
	 * Builder for {@link DockerService}.
	 */
	public static class Builder {

		private String image;

		private String imageTag = "latest";

		private String website;

		private String command;

		private final Set<Integer> ports = new TreeSet<>();

		protected Builder(String imageAndTag) {
			String[] split = imageAndTag.split(":", 2);
			String tag = (split.length == 1) ? "latest" : split[1];
			image(split[0]).imageTag(tag);
		}

		public Builder image(String image) {
			this.image = image;
			return this;
		}

		public Builder imageTag(String imageTag) {
			this.imageTag = imageTag;
			return this;
		}

		public Builder website(String website) {
			this.website = website;
			return this;
		}

		public Builder command(String command) {
			this.command = command;
			return this;
		}

		public Builder ports(Collection<Integer> ports) {
			this.ports.addAll(ports);
			return this;
		}

		public Builder ports(int... ports) {
			return ports(Arrays.stream(ports).boxed().toList());
		}

		/**
		 * Build a {@link DockerService} with the current state of this builder.
		 * @return a {@link DockerService}
		 */
		public DockerService build() {
			return new DockerService(this);
		}

	}

}
