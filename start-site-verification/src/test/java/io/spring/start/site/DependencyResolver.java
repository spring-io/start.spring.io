/*
 * Copyright 2019 the original author or authors.
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.spring.initializr.generator.version.Version;
import io.spring.initializr.metadata.BillOfMaterials;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.GetTask;
import org.eclipse.aether.spi.connector.transport.PeekTask;
import org.eclipse.aether.spi.connector.transport.PutTask;
import org.eclipse.aether.spi.connector.transport.Transporter;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.transfer.NoTransporterException;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.filter.DependencyFilterUtils;
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy;

import org.springframework.util.FileSystemUtils;

final class DependencyResolver {

	private static final Collection<DependencyResolver> instances = new ArrayList<>();

	private static final ThreadLocal<DependencyResolver> instanceForThread = ThreadLocal.withInitial(() -> {
		DependencyResolver instance = new DependencyResolver();
		instances.add(instance);
		return instance;
	});

	private static final RepositoryPolicy repositoryPolicy = new RepositoryPolicy(true,
			RepositoryPolicy.UPDATE_POLICY_NEVER, RepositoryPolicy.CHECKSUM_POLICY_IGNORE);

	private static final RemoteRepository mavenCentral = new RemoteRepository.Builder("central", "default",
			"https://repo1.maven.org/maven2").setReleasePolicy(repositoryPolicy).build();

	private static final RemoteRepository springMilestones = new RemoteRepository.Builder("spring-milestones",
			"default", "https://repo.spring.io/milestone").setReleasePolicy(repositoryPolicy).build();

	private static final RemoteRepository springSnapshots = new RemoteRepository.Builder("spring-snapshots", "default",
			"https://repo.spring.io/snapshot").setSnapshotPolicy(repositoryPolicy).build();

	private static final Map<String, List<Dependency>> managedDependencies = new ConcurrentHashMap<>();

	private final Path localRepositoryLocation;

	private final RepositorySystemSession repositorySystemSession;

	private final RepositorySystem repositorySystem;

	DependencyResolver() {
		try {
			ServiceLocator serviceLocator = createServiceLocator();
			DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
			session.setArtifactDescriptorPolicy(new SimpleArtifactDescriptorPolicy(false, false));
			this.localRepositoryLocation = Files.createTempDirectory("metadata-validation-m2");
			LocalRepository localRepository = new LocalRepository(this.localRepositoryLocation.toFile());
			this.repositorySystem = serviceLocator.getService(RepositorySystem.class);
			session.setLocalRepositoryManager(
					this.repositorySystem.newLocalRepositoryManager(session, localRepository));
			session.setReadOnly();
			this.repositorySystemSession = session;
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	static List<String> resolveDependencies(String groupId, String artifactId, String version, Version bootVersion,
			List<BillOfMaterials> boms) {
		DependencyResolver instance = instanceForThread.get();
		List<Dependency> managedDependencies = instance.getManagedDependencies(boms, bootVersion);
		Dependency aetherDependency = new Dependency(new DefaultArtifact(groupId, artifactId, "pom",
				instance.getVersion(groupId, artifactId, version, managedDependencies)), "compile");
		CollectRequest collectRequest = new CollectRequest((org.eclipse.aether.graph.Dependency) null,
				Collections.singletonList(aetherDependency), instance.getRepositoriesForVersion(bootVersion));
		collectRequest.setManagedDependencies(managedDependencies);
		DependencyRequest dependencyRequest = new DependencyRequest(collectRequest,
				DependencyFilterUtils.classpathFilter(JavaScopes.COMPILE, JavaScopes.RUNTIME));
		try {
			return instance.resolveDependencies(dependencyRequest).getArtifactResults().stream()
					.map(ArtifactResult::getArtifact)
					.map((artifact) -> artifact.getGroupId() + ":" + artifact.getArtifactId())
					.collect(Collectors.toList());
		}
		catch (DependencyResolutionException ex) {
			throw new RuntimeException(ex);
		}
	}

	static void cleanUp() {
		instances.forEach(DependencyResolver::deleteLocalRepository);
	}

	void deleteLocalRepository() {
		try {
			FileSystemUtils.deleteRecursively(this.localRepositoryLocation);
		}
		catch (IOException ex) {
			// Continue
		}
	}

	private List<Dependency> getManagedDependencies(List<BillOfMaterials> boms, Version bootVersion) {
		return boms.stream().flatMap(
				(bom) -> getManagedDependencies(bom.getGroupId(), bom.getArtifactId(), bom.getVersion(), bootVersion))
				.collect(Collectors.toList());
	}

	private Stream<Dependency> getManagedDependencies(String groupId, String artifactId, String version,
			Version bootVersion) {
		String key = groupId + ":" + artifactId + ":" + version;
		List<org.eclipse.aether.graph.Dependency> managedDependencies = DependencyResolver.managedDependencies
				.computeIfAbsent(key,
						(coords) -> resolveManagedDependencies(groupId, artifactId, version, bootVersion));
		return managedDependencies.stream();
	}

	private List<org.eclipse.aether.graph.Dependency> resolveManagedDependencies(String groupId, String artifactId,
			String version, Version bootVersion) {
		try {
			List<RemoteRepository> repositories = getRepositoriesForVersion(bootVersion);
			return this.repositorySystem
					.readArtifactDescriptor(this.repositorySystemSession, new ArtifactDescriptorRequest(
							new DefaultArtifact(groupId, artifactId, "pom", version), repositories, null))
					.getManagedDependencies();
		}
		catch (ArtifactDescriptorException ex) {
			throw new RuntimeException(ex);
		}
	}

	private List<RemoteRepository> getRepositoriesForVersion(Version bootVersion) {
		List<RemoteRepository> repositories = new ArrayList<>();
		repositories.add(mavenCentral);
		if (!bootVersion.getQualifier().getQualifier().equals("RELEASE")) {
			repositories.add(springMilestones);
			if (bootVersion.getQualifier().getQualifier().contains("SNAPSHOT")) {
				repositories.add(springSnapshots);
			}
		}
		return repositories;
	}

	private DependencyResult resolveDependencies(DependencyRequest dependencyRequest)
			throws DependencyResolutionException {
		DependencyResult resolved = this.repositorySystem.resolveDependencies(this.repositorySystemSession,
				dependencyRequest);
		return resolved;
	}

	private String getVersion(String groupId, String artifactId, String version,
			List<org.eclipse.aether.graph.Dependency> managedDependencies) {
		if (version != null) {
			return version;
		}
		for (org.eclipse.aether.graph.Dependency managedDependency : managedDependencies) {
			if (groupId.equals(managedDependency.getArtifact().getGroupId())
					&& artifactId.equals(managedDependency.getArtifact().getArtifactId())) {
				return managedDependency.getArtifact().getVersion();
			}
		}
		return null;
	}

	private static ServiceLocator createServiceLocator() {
		DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
		locator.addService(RepositorySystem.class, DefaultRepositorySystem.class);
		locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
		locator.addService(TransporterFactory.class, DependencyResolver.JarSkippingHttpTransporterFactory.class);
		return locator;
	}

	private static class JarSkippingHttpTransporterFactory implements TransporterFactory {

		private final HttpTransporterFactory delegate = new HttpTransporterFactory();

		@Override
		public Transporter newInstance(RepositorySystemSession session, RemoteRepository repository)
				throws NoTransporterException {
			return new JarGetSkippingTransporter(this.delegate.newInstance(session, repository));
		}

		@Override
		public float getPriority() {
			return 5.0f;
		}

		private static final class JarGetSkippingTransporter implements Transporter {

			private final Transporter delegate;

			private JarGetSkippingTransporter(Transporter delegate) {
				this.delegate = delegate;
			}

			@Override
			public int classify(Throwable error) {
				return this.delegate.classify(error);
			}

			@Override
			public void peek(PeekTask task) throws Exception {
				this.delegate.peek(task);
			}

			@Override
			public void get(GetTask task) throws Exception {
				if (task.getLocation().getPath().endsWith(".jar")) {
					return;
				}
				this.delegate.get(task);
			}

			@Override
			public void put(PutTask task) throws Exception {
				this.delegate.put(task);
			}

			@Override
			public void close() {
				this.delegate.close();
			}

		}

	}

}
