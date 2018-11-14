/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.start.site.extension;

import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.Dependency;
import org.junit.Test;

/**
 * Tests for {@link KafkaTestRequestPostProcessor}.
 *
 * @author wonwoo lee
 */

public class KafkaTestRequestPostProcessorTests
		extends AbstractRequestPostProcessorTests {

    @Test
    public void kafkaTestIsAdded() {
        ProjectRequest request = createProjectRequest("kafka");
        request.setBootVersion("1.5.0.RELEASE");
        Dependency kafkaTest = Dependency.withId("spring-kafka-test",
        		"org.springframework.kafka", "spring-kafka-test",
        		null, Dependency.SCOPE_TEST);
        generateMavenPom(request)
        		.hasSpringBootStarterTest().hasDependency(kafkaTest)
        		.hasDependenciesCount(4);
    }

    @Test
    public void kafkaTestIsNotAddedWithoutSpringKafka() {
        ProjectRequest request = createProjectRequest("web");
        generateMavenPom(request).hasSpringBootStarterDependency("web")
        		.hasSpringBootStarterTest().hasDependenciesCount(2);
    }
}