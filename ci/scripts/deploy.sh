#!/bin/bash
set -e

CONFIG_DIR=git-repo/ci/config
echo $KEY > key.json
export GOOGLE_APPLICATION_CREDENTIALS=key.json
export KUBECONFIG=${CONFIG_DIR}/kubeconfig.yml
export ENCODED_uri=$(echo -n $ELASTIC_uri | base64)
ytt -f ${CONFIG_DIR}/secret.yml -f ${CONFIG_DIR}/elastic.yml --data-values-env ENCODED | kubectl apply -f-
kubectl apply -f ${CONFIG_DIR}/deployment.yml
