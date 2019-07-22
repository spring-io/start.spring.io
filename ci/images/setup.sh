#!/bin/bash
set -ex

###########################################################
# UTILS
###########################################################

apt-get update
apt-get install -y curl

curl https://raw.githubusercontent.com/spring-io/concourse-java-scripts/v0.0.2/concourse-java.sh > /opt/concourse-java.sh


###########################################################
# JAVA
###########################################################

mkdir -p /opt/openjdk
cd /opt/openjdk
curl -L https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u212-b04/OpenJDK8U-jdk_x64_linux_hotspot_8u212b04.tar.gz | tar zx --strip-components=1
test -f /opt/openjdk/bin/java
test -f /opt/openjdk/bin/javac
