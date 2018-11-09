#!/bin/bash
set -e

pushd git-repo > /dev/null
./mvnw clean -f pom.xml clean package -U
popd > /dev/null

cp git-repo/target/start-site.jar output/start-site.jar
