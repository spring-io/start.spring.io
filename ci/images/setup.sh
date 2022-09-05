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
curl -L https://github.com/bell-sw/Liberica/releases/download/17.0.4.1+1/bellsoft-jdk17.0.4.1+1-linux-amd64.tar.gz | tar zx --strip-components=1
test -f /opt/openjdk/bin/java
test -f /opt/openjdk/bin/javac
