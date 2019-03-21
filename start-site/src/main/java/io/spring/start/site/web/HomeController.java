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

package io.spring.start.site.web;

import java.beans.PropertyDescriptor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.samskivert.mustache.Mustache;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.DependencyGroup;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.metadata.TypeCapability;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * UI specific controller providing dedicated endpoints for the Web UI.
 *
 * @author Stephane Nicoll
 */
@Controller
public class HomeController {

	protected final InitializrMetadataProvider metadataProvider;

	protected final ResourceUrlProvider resourceUrlProvider;

	private Boolean forceSsl;

	private final Function<String, String> linkTo;

	public HomeController(InitializrMetadataProvider metadataProvider,
			ResourceUrlProvider resourceUrlProvider) {
		this.metadataProvider = metadataProvider;
		this.resourceUrlProvider = resourceUrlProvider;
		this.linkTo = (link) -> {
			String result = resourceUrlProvider.getForLookupPath(link);
			return (result != null) ? result : link;
		};

	}

	@ModelAttribute("linkTo")
	public Mustache.Lambda linkTo() {
		return (frag, out) -> out.write(this.linkTo.apply(frag.execute()));
	}

	@GetMapping(path = "/", produces = "text/html")
	public String home(HttpServletRequest request, Map<String, Object> model) {
		if (isForceSsl() && !request.isSecure()) {
			String securedUrl = ServletUriComponentsBuilder.fromCurrentRequest()
					.scheme("https").build().toUriString();
			return "redirect:" + securedUrl;
		}
		renderHome(model);
		return "home";
	}

	/**
	 * Render the home page with the specified template.
	 * @param model the model data
	 */
	protected void renderHome(Map<String, Object> model) {
		InitializrMetadata metadata = this.metadataProvider.get();

		model.put("serviceUrl", generateAppUrl());
		BeanWrapperImpl wrapper = new BeanWrapperImpl(metadata);
		for (PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
			if ("types".equals(descriptor.getName())) {
				model.put("types", removeTypes(metadata.getTypes()));
			}
			else {
				model.put(descriptor.getName(),
						wrapper.getPropertyValue(descriptor.getName()));
			}
		}

		// Google analytics support
		model.put("trackingCode",
				metadata.getConfiguration().getEnv().getGoogleAnalyticsTrackingCode());

	}

	/**
	 * Generate a full URL of the service, mostly for use in templates.
	 * @return the app URL
	 * @see io.spring.initializr.metadata.InitializrConfiguration.Env#isForceSsl()
	 */
	protected String generateAppUrl() {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
				.fromCurrentServletMapping();
		if (isForceSsl()) {
			builder.scheme("https");
		}
		return builder.build().toString();
	}

	private TypeCapability removeTypes(TypeCapability types) {
		TypeCapability result = new TypeCapability();
		result.setDescription(types.getDescription());
		result.setTitle(types.getTitle());
		result.getContent().addAll(types.getContent());
		// Only keep project type
		result.getContent().removeIf((t) -> !"project".equals(t.getTags().get("format")));
		return result;
	}

	private boolean isForceSsl() {
		if (this.forceSsl == null) {
			this.forceSsl = this.metadataProvider.get().getConfiguration().getEnv()
					.isForceSsl();
		}
		return this.forceSsl;

	}

	@GetMapping(path = "/ui/dependencies", produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> dependencies() {
		List<DependencyGroup> dependencyGroups = this.metadataProvider.get()
				.getDependencies().getContent();
		List<DependencyItem> content = new ArrayList<>();
		dependencyGroups
				.forEach((group) -> group.getContent().forEach((dependency) -> content
						.add(new DependencyItem(group.getName(), dependency))));
		String json = writeDependencies(content);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
				.eTag(createUniqueId(json)).body(json);
	}

	private static String writeDependencies(List<DependencyItem> items) {
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		ArrayNode maps = JsonNodeFactory.instance.arrayNode();
		items.forEach((dependency) -> maps.add(mapDependency(dependency)));
		json.set("dependencies", maps);
		return json.toString();
	}

	private static ObjectNode mapDependency(DependencyItem item) {
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		Dependency dependency = item.dependency;
		node.put("id", dependency.getId());
		node.put("name", dependency.getName());
		node.put("group", item.group);
		node.put("weight", dependency.getWeight());
		if (dependency.getDescription() != null) {
			node.put("description", dependency.getDescription());
		}
		if (dependency.getVersionRange() != null) {
			node.put("versionRange", dependency.getVersionRange());
			node.put("versionRequirement", dependency.getVersionRequirement());
		}
		if (!CollectionUtils.isEmpty(dependency.getKeywords())
				|| !CollectionUtils.isEmpty(dependency.getAliases())) {
			List<String> all = new ArrayList<>(dependency.getKeywords());
			all.addAll(dependency.getAliases());
			node.put("keywords", StringUtils.collectionToCommaDelimitedString(all));
		}
		return node;
	}

	private String createUniqueId(String content) {
		StringBuilder builder = new StringBuilder();
		DigestUtils.appendMd5DigestAsHex(content.getBytes(StandardCharsets.UTF_8),
				builder);
		return builder.toString();
	}

	private static class DependencyItem {

		private final String group;

		private final Dependency dependency;

		DependencyItem(String group, Dependency dependency) {
			this.group = group;
			this.dependency = dependency;
		}

	}

}
