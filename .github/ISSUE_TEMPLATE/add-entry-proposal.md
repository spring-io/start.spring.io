---
name: New Entry Proposal
about: Suggest a new entry on start.spring.io.
labels: 'type: entry-proposal'
---

# Requirements
To ensure the ongoing health of the Spring Boot community, a starter that is included on
start.spring.io must meet the following requirements.

## Code of Conduct
This project adheres to the Contributor Covenant code of conduct. By participating, you
are expected to adhere to this code. If you have not already done so, we also encourage
you to adopt a code of conduct in your own project.

## Well-established project with a vibrant community
To avoid overwhelming users with too much choice, we aim for each starter to be of
interest to a broad range of start.spring.io's users. One way to ensure this is for each
starter to represent a well-established project with a vibrant community.

Please provide some links that provide some evidence for this. We're looking for
things like GitHub stars, multiple contributors, ongoing maintenance, Stack Overflow
questions, etc.

## Ongoing maintenance
To ensure that the Spring Boot community has a good experience with your starter,
it should be kept up-to-date with the latest Spring Boot releases and maintenance
releases should be shipped as needed. When a new release is made, a pull request
should be made to this project to ensure that the Spring Boot community are using the
latest version

## Licence
Please provide details of your project's licence. If you starter pulls in any
dependencies with a different license to the  starter's own licence, please provide
details of those dependencies' licences as well.

## Issue Tracker URL
To allow users of your starter to contribute and provide feedback, please provide
the URL of its issue tracker.

## Continuous integration
To help to ensure the ongoing compatibility of your starter with Spring Boot, please
provide a link to the CI jobs that verify its compatibility. Compatibility should
be achieved using Spring Boot's dependency management without overriding.

## Maven Central
Starters that are included on start.spring.io must be available on Maven Central.

[ ] Starter and all of its dependencies are available on Maven Central.

## Configuration metadata
If you starter provides `@ConfigurationProperties` it should provide metadata for those
properties to enable auto-completion in the IDE. Metadata can be provided by adding
field level documentation on each of the properties and adding the
`spring-boot-configuration-processor` to your build.

[ ] Starter provides metadata, including documentation, for all its configuration
properties.

## Build system integration
If your starter provides build system integration in the form of custom plugins or
configuration, both Maven and Gradle must be supported.

Please provide the details of any custom build configuration that should be generated
when your proposed entry is selected.

## Version mappings
Spring Initializr allows different versions of a starter to be used with different
versions of Spring Boot. For example, if a project is being generated with Spring
Boot version W you may want to use Starter version X and if a project is being
generated with Spring Boot version Y you may want to use Starter version Z.

Please provide the details of any mappings between particular version of Spring
Boot and particular versions of your starter.

## Links to additional resources
Each entry can be associated with one or several links. Please provide at least a link to
a sample and the reference documentation:

* Reference documentation:
* Sample:
