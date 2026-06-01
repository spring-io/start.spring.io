# Tanzu Spring SDK Dependency Verification Examples

## Configuration Added to start.spring.io

### 1. BOM Definition (application.yml)
```yaml
tanzu-spring-sdk:
  groupId: com.vmware.tanzu.spring
  artifactId: tanzu-spring-sdk-dependencies
  versionProperty: tanzu-spring-sdk.version
  mappings:
    - compatibilityRange: "[3.5.0,4.0.0)"
      version: 1.0.0
```

### 2. Dependency Definition (application.yml)
```yaml
- name: Tanzu Spring SDK [Enterprise]
  id: tanzu-spring-sdk
  bom: tanzu-spring-sdk
  description: Provides JVM metrics for Tanzu Hub and OpenFeature + Enterprise Application Configuration feature flags integration for Spring Boot applications.
  groupId: com.vmware.tanzu.spring
  artifactId: tanzu-spring-starter
  facets:
    - tanzu-spring-enterprise
  links:
    - rel: reference
      href: https://techdocs.broadcom.com/us/en/vmware-tanzu/spring/tanzu-spring/commercial/spring-tanzu/tanzu-spring-sdk-getting-started.html
```

## What Users Will See

### In the Web UI:
- **Group**: VMware Tanzu Spring Enterprise Extensions
- **Name**: Tanzu Spring SDK [Enterprise] 
- **Description**: Provides JVM metrics for Tanzu Hub and OpenFeature + Enterprise Application Configuration feature flags integration for Spring Boot applications.
- **Available**: Spring Boot 3.5.0 and above only
- **Enterprise Badge**: [Enterprise] designation

### Generated Maven Project (pom.xml):
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

### Generated Gradle Project (build.gradle):
```gradle
dependencies {
    implementation platform('com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:1.0.0')
    implementation 'com.vmware.tanzu.spring:tanzu-spring-starter'
}
```

### Generated HELP.md Section:
```markdown
## VMware Tanzu Spring Enterprise Extensions

You have selected to add [Tanzu Spring](https://www.vmware.com/products/app-platform/tanzu-spring) enterprise extensions to your project.
In order to use these enterprise extensions you must have authorized [access to the Spring Enterprise Repository](https://techdocs.broadcom.com/us/en/vmware-tanzu/spring/tanzu-spring/commercial/spring-tanzu/guide-artifact-repository-administrators.html) artifacts with an entitlement to Tanzu Spring.
To learn more about what is included with Tanzu Spring entitlement, check out [enterprise.spring.io](https://enterprise.spring.io/) for more information.
```

## Verification Complete ✅

The new Tanzu Spring SDK Enterprise dependency has been successfully added to start.spring.io with:

- ✅ Proper BOM version management
- ✅ Enterprise classification and faceting
- ✅ Automatic help documentation 
- ✅ Spring Boot 3.5.0+ compatibility
- ✅ Official Broadcom documentation links
- ✅ Maven and Gradle support
- ✅ Following existing Enterprise dependency patterns

The configuration is ready for deployment to the production start.spring.io instance.