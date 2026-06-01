# Tanzu Spring SDK Enterprise Dependency Demo

## Overview
This demonstrates the new Tanzu Spring SDK Enterprise dependency that has been added to start.spring.io.

## What gets generated

When users select the "Tanzu Spring SDK [Enterprise]" dependency, the generated project will include:

### Maven pom.xml
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.vmware.tanzu.spring</groupId>
            <artifactId>tanzu-spring-sdk-dependencies</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>com.vmware.tanzu.spring</groupId>
        <artifactId>tanzu-spring-starter</artifactId>
    </dependency>
</dependencies>
```

### Gradle build.gradle
```gradle
dependencies {
    implementation platform("com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:1.0.0")
    implementation "com.vmware.tanzu.spring:tanzu-spring-starter"
}
```

### HELP.md Section
```markdown
## VMware Tanzu Spring Enterprise Extensions

You have selected to add [Tanzu Spring](https://www.vmware.com/products/app-platform/tanzu-spring) enterprise extensions to your project.
In order to use these enterprise extensions you must have authorized [access to the Spring Enterprise Repository](https://techdocs.broadcom.com/us/en/vmware-tanzu/spring/tanzu-spring/commercial/spring-tanzu/guide-artifact-repository-administrators.html) artifacts with an entitlement to Tanzu Spring.
To learn more about what is included with Tanzu Spring entitlement, check out [enterprise.spring.io](https://enterprise.spring.io/) for more information.
```

## Features Provided

The Tanzu Spring SDK provides:

1. **JVM Metrics for Tanzu Hub** - Automatically configured metrics collection and reporting
2. **OpenFeature Integration** - Enterprise Application Configuration feature flags support
3. **Enterprise Repository Access** - Requires proper entitlement and repository configuration

## Usage Requirements

- Spring Boot 3.5.0 or higher
- Valid Tanzu Spring entitlement
- Access to Spring Enterprise Repository
- Proper repository configuration in build system

## Documentation Reference

Official getting started guide: https://techdocs.broadcom.com/us/en/vmware-tanzu/spring/tanzu-spring/commercial/spring-tanzu/tanzu-spring-sdk-getting-started.html