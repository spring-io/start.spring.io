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
package io.spring.start.site.container;

import io.spring.initializr.generator.container.docker.compose.PortMapping;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link SimpleDockerServiceResolver}.
 *
 * @author Venkata Naga Sai Srikanth Gollapudi
 */

public class SimpleDockerServiceResolverTests {
    private final SimpleDockerServiceResolver resolver = new SimpleDockerServiceResolver();

     @Test
     void elasticSearchUsesDockerHubImage() {
        DockerService service = this.resolver.resolve("elasticsearch");
        assertThat(service).isNotNull();
        assertThat(service.getImage()).isEqualTo("elasticsearch");
        assertThat(service.getImageTag()).isEqualTo("7.17.10");
        assertThat(service.getWebsite()).isEqualTo("https://hub.docker.com/_/elasticsearch");
     }
     @Test
     void elasticSearchExposesExpectedPorts() {
         DockerService service = this.resolver.resolve("elasticsearch");
         assertThat(service).isNotNull();
         assertThat(service.getPorts()).extracting(PortMapping::getContainerPort).containsExactly(9200, 9300);
     }

     @Test
     void shouldReturnNullForUnknownService() {
         assertThat(this.resolver.resolve("unknown-service")).isNull();
     }

}
