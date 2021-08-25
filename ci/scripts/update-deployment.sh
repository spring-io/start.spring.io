#!/bin/bash
set -e

IMAGE=$( cat kpack/image )

git clone git-repo updated-git-repo > /dev/null

pushd updated-git-repo > /dev/null
CONFIG_DIR=ci/config
sed -i -e "s|image: .*|image: ${IMAGE}|" ${CONFIG_DIR}/deployment.yml

git config user.name "Spring Builds" > /dev/null
git config user.email "spring-builds@users.noreply.github.com" > /dev/null
git add ${CONFIG_DIR}/deployment.yml > /dev/null
git commit -m"Update image digest in deployment.yml" > /dev/null

popd > /dev/null