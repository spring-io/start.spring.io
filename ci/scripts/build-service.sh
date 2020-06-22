#!/bin/bash
set -e

source $(dirname $0)/common.sh

repository=$(pwd)/distribution-repository
pushd git-repo
./mvnw clean deploy -U -DaltDeploymentRepository=distribution::default::file://${repository}
popd
