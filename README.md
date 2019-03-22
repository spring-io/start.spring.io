<p align="center">
  <a href="https://start.spring.io">
    <img alt="start.spring.io" title="start.spring.io" src="https://i.imgur.com/vbNT4m0.png" width="450">
  </a>
</p>

<p align="center">
  <a href="https://gitter.im/spring-io/initializr?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge">
    <img src="https://badges.gitter.im/spring-io/initializr.svg"
         alt="Gitter">
  </a>
</p>

## Introduction

This repository configures a [Spring Initializr][] instance with a custom UI running at https://start.spring.io. 
The following defines are available:

* `start-client`: client-side assets
* `start-site`: server infrastructure and metadata configuration

---

## How to use

### Building from Source

You need Java 1.8 and a bash-like shell.

#### Building

Invoke the build at the root of the project:

    $ ./mvnw clean install

The project also has Selenium tests that instrument the client side. In order to run them
you need first to install Firefox and the latest [geckodriver][].

Once those are installed you can run the smoke tests by enabling an extra profile:

    $ ./mvnw verify -PsmokeTests

#### Running the app locally

You can easily start the app as any other Spring Boot app:

    $ ./mvnw spring-boot:run

#### Running the app in an IDE

You should be able to import the project into your IDE with no problems. Once there you
can run the `StartApplication` from its main method and debug it. If you also need to
work on the library, adding the `initializr` project in your workspace would make sure
to reload the app whenever you make any change.

This is the recommended way to operate while you are developing the application,
especially the UI.

### Deploying to Cloud Foundry

If you are on a Mac and using [homebrew][], install the Cloud Foundry
CLI:

    $ brew install cloudfoundry-cli

Alternatively, download a suitable binary for your platform from [Pivotal Web Services][].

You should ensure that the application name and URL (name and host values) are
suitable for your environment before running `cf push`.

First, make sure that you have [building, built the application](#building), then make sure first
that the jar has been created:

    $ ./mvnw package

Once the jar has been created, you can push the application:

    $ cf push your-start -p spring-site/target/start-site.jar

---

## License
The start.spring.io website is Open Source software released under the [Apache 2.0 license][].

---

This project is powered by:

<a href="http://pivotal.io/"><img alt="Pivotal" width="136" title="Pivotal" src="https://i.imgur.com/XPeBw7A.png"></a> 
<a href="http://spring.io/"><img alt="Spring" title="Spring" src="https://i.imgur.com/az8Xady.png" width="155"></a>

[Spring Initializr]: https://github.com/spring-io/initializr
[geckodriver]: https://github.com/mozilla/geckodriver
[homebrew]: https://brew.sh/
[Pivotal Web Services]: https://console.run.pivotal.io/tools
[Apache 2.0 license]: https://www.apache.org/licenses/LICENSE-2.0.html