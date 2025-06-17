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

package io.spring.start.site;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import io.spring.initializr.metadata.BillOfMaterials;
import io.spring.start.testsupport.Homes;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyFilter;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RemoteRepository.Builder;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transfer.TransferResource;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;
import org.eclipse.aether.util.graph.visitor.FilteringDependencyVisitor;
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class DependencyResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DependencyResolver.class);

	static final RemoteRepository mavenCentral = createRemoteRepository("central", "https://repo1.maven.org/maven2",
			false);

	private static final Map<String, List<Dependency>> managedDependencies = new ConcurrentHashMap<>();

	private final RepositorySystemSession repositorySystemSession;

	private final RepositorySystem repositorySystem;

	@SuppressWarnings("deprecation")
	DependencyResolver(Path localRepositoryLocation) {
		try {
			ServiceLocator serviceLocator = createServiceLocator();
			DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
			session.setTransferListener(new Slf4jTransferListener());
			session.setArtifactDescriptorPolicy(new SimpleArtifactDescriptorPolicy(false, false));
			LocalRepository localRepository = new LocalRepository(localRepositoryLocation.toFile());
			this.repositorySystem = serviceLocator.getService(RepositorySystem.class);
			session
				.setLocalRepositoryManager(this.repositorySystem.newLocalRepositoryManager(session, localRepository));
			session.setUserProperties(System.getProperties());
			session.setIgnoreArtifactDescriptorRepositories(true);
			session.setReadOnly();
			this.repositorySystemSession = session;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private static RepositoryPolicy repositoryPolicy(boolean enabled) {
		return new RepositoryPolicy(enabled, RepositoryPolicy.UPDATE_POLICY_NEVER,
				RepositoryPolicy.CHECKSUM_POLICY_IGNORE);
	}

	static RemoteRepository createRemoteRepository(String id, String url, boolean snapshot) {
		Builder repositoryBuilder = new Builder(id, "default", url);
		repositoryBuilder.setSnapshotPolicy(repositoryPolicy(snapshot));
		repositoryBuilder.setReleasePolicy(repositoryPolicy(!snapshot));
		return repositoryBuilder.build();
	}

	static List<String> resolveDependencies(Homes homes, String groupId, String artifactId, String version,
			List<BillOfMaterials> boms, List<RemoteRepository> repositories) {
		DependencyResolver resolver = new DependencyResolver(homes.get().resolve("repository"));
		List<Dependency> managedDependencies = resolver.getManagedDependencies(boms, repositories);
		Dependency aetherDependency = new Dependency(new DefaultArtifact(groupId, artifactId, "pom",
				resolver.getVersion(groupId, artifactId, version, managedDependencies)), "compile");
		CollectRequest collectRequest = new CollectRequest(aetherDependency, repositories);
		collectRequest.setManagedDependencies(managedDependencies);
		try {
			CollectResult result = resolver.collectDependencies(collectRequest);
			return DependencyCollector.collect(result.getRoot(), RuntimeTransitiveOnlyDependencyFilter.INSTANCE);
		}
		catch (DependencyCollectionException ex) {
			throw new RuntimeException(ex);
		}
	}

	private List<Dependency> getManagedDependencies(List<BillOfMaterials> boms, List<RemoteRepository> repositories) {
		return boms.stream()
			.flatMap((bom) -> getManagedDependencies(bom.getGroupId(), bom.getArtifactId(), bom.getVersion(),
					repositories))
			.toList();
	}

	private Stream<Dependency> getManagedDependencies(String groupId, String artifactId, String version,
			List<RemoteRepository> repositories) {
		String key = groupId + ":" + artifactId + ":" + version;
		List<Dependency> managedDependencies = DependencyResolver.managedDependencies.computeIfAbsent(key,
				(coords) -> resolveManagedDependencies(groupId, artifactId, version, repositories));
		return managedDependencies.stream();
	}

	private List<Dependency> resolveManagedDependencies(String groupId, String artifactId, String version,
			List<RemoteRepository> repositories) {
		try {
			return this.repositorySystem
				.readArtifactDescriptor(this.repositorySystemSession,
						new ArtifactDescriptorRequest(new DefaultArtifact(groupId, artifactId, "pom", version),
								repositories, null))
				.getManagedDependencies();
		}
		catch (ArtifactDescriptorException ex) {
			throw new RuntimeException(ex);
		}
	}

	private CollectResult collectDependencies(CollectRequest dependencyRequest) throws DependencyCollectionException {
		return this.repositorySystem.collectDependencies(this.repositorySystemSession, dependencyRequest);
	}

	private String getVersion(String groupId, String artifactId, String version, List<Dependency> managedDependencies) {
		if (version != null) {
			return version;
		}
		for (Dependency managedDependency : managedDependencies) {
			if (groupId.equals(managedDependency.getArtifact().getGroupId())
					&& artifactId.equals(managedDependency.getArtifact().getArtifactId())) {
				return managedDependency.getArtifact().getVersion();
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private static ServiceLocator createServiceLocator() {
		DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
		locator.addService(RepositorySystem.class, DefaultRepositorySystem.class);
		locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
		locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
		return locator;
	}

	static class DependencyCollector implements DependencyVisitor {

		private final List<String> dependencies = new ArrayList<>();

		static List<String> collect(DependencyNode node, DependencyFilter filter) {
			DependencyCollector collector = new DependencyCollector();
			node.accept(new FilteringDependencyVisitor(collector, filter));
			return collector.dependencies;
		}

		@Override
		public boolean visitEnter(DependencyNode node) {
			return this.dependencies.add(node.getDependency().getArtifact().getGroupId() + ":"
					+ node.getDependency().getArtifact().getArtifactId());
		}

		@Override
		public boolean visitLeave(DependencyNode node) {
			return true;
		}

	}

	static class RuntimeTransitiveOnlyDependencyFilter implements DependencyFilter {

		private static final RuntimeTransitiveOnlyDependencyFilter INSTANCE = new RuntimeTransitiveOnlyDependencyFilter();

		private final DependencyFilter runtimeFilter = DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE,
				JavaScopes.RUNTIME);

		@Override
		public boolean accept(DependencyNode node, List<DependencyNode> parents) {
			return !node.getDependency().isOptional() && this.runtimeFilter.accept(node, parents);
		}

	}

	private static final class Slf4jTransferListener extends AbstractTransferListener {

		@Override
		public void transferStarted(TransferEvent event) {
			LOGGER.info("Started downloading {}", resourceToString(event.getResource()));
		}

		@Override
		public void transferCorrupted(TransferEvent event) {
			LOGGER.warn("Found corrupted download {}", resourceToString(event.getResource()));
		}

		@Override
		public void transferSucceeded(TransferEvent event) {
			LOGGER.info("Done downloading {} bytes for {}", event.getTransferredBytes(),
					resourceToString(event.getResource()));
		}

		@Override
		public void transferFailed(TransferEvent event) {
			LOGGER.info("Failed downloading {}", resourceToString(event.getResource()));
		}

		private String resourceToString(TransferResource resource) {
			return resource.getRepositoryUrl() + resource.getResourceName();
		}

	}

}
