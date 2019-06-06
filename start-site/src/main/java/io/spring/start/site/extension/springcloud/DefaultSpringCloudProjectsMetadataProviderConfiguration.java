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

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for {@link DefaultSpringCloudProjectsMetadataProvider}.
 *
 * @author Olga Maciaszek-Sharma
 */
@Configuration
@ConditionalOnMissingBean(SpringCloudProjectsMetadataProvider.class)
public class DefaultSpringCloudProjectsMetadataProviderConfiguration {

	@Bean
	SpringCloudProjectGenerationProperties springCloudProjectGenerationProperties() {
		return new SpringCloudProjectGenerationProperties();
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	SpringCloudProjectsMetadataProvider springCloudProjectsMetadataProvider() {
		return new DefaultSpringCloudProjectsMetadataProvider(restTemplate(),
				springCloudProjectGenerationProperties());
	}

	@Bean
	@ConditionalOnClass(javax.cache.CacheManager.class)
	JCacheManagerCustomizer springCloudCacheManagerCustomizer() {
		return (cacheManager) -> cacheManager
				.createCache("start.spring-cloud-projects-metadata", config());
	}

	private MutableConfiguration<Object, Object> config() {
		return new MutableConfiguration<>().setStoreByValue(false)
				.setManagementEnabled(true).setStatisticsEnabled(true)
				.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_HOUR));
	}

}
