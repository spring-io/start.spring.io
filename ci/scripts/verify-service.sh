#!/bin/bash
set -e

source $(dirname $0)/common.sh

pushd git-repo > /dev/null
./mvnw -Pverification -U -f pom.xml clean verify
popd > /dev/null
