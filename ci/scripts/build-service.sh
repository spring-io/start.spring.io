#!/bin/bash
set -e

source $(dirname $0)/common.sh

pushd git-repo > /dev/null
./mvnw clean -f pom.xml clean package -U
popd > /dev/null

cp git-repo/target/start-site.jar output/start-site.jar
