#!/bin/bash
set -ex

###########################################################
# UTILS
###########################################################

apt-get update
apt-get install -y curl git

curl https://raw.githubusercontent.com/spring-io/concourse-java-scripts/v0.0.2/concourse-java.sh > /opt/concourse-java.sh


###########################################################
# JAVA
###########################################################

mkdir -p /opt/openjdk
cd /opt/openjdk
curl -L https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.8%2B10/OpenJDK11U-jdk_x64_linux_hotspot_11.0.8_10.tar.gz | tar zx --strip-components=1
test -f /opt/openjdk/bin/java
test -f /opt/openjdk/bin/javac
