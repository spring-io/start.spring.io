#!/bin/bash
set -e

pushd git-repo
snapshotVersion=0.0.1-SNAPSHOT
popd
pushd artifactory-repo/io/spring/start/start-site/$snapshotVersion
path=$( find . -type f -iname "*-exec.jar" )
name=${path#"./"}
popd
echo $name
echo "${ARTIFACTORY_SERVER}/${ARTIFACTORY_REPO}/io/spring/start/start-site/${snapshotVersion}/$name" > blob-url/url