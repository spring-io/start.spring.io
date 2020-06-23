#!/bin/bash
set -e

CONFIG_DIR=git-repo/ci/k8s/config
echo $KEY > key.json
export GOOGLE_APPLICATION_CREDENTIALS=key.json
export KUBECONFIG=${CONFIG_DIR}/kubeconfig.yml

ytt -f ${CONFIG_DIR}/secret.yml -f ${CONFIG_DIR}/elastic.yml --data-values-env TEST | kubectl apply -f-
kubectl apply -f ${CONFIG_DIR}/deployment.yml
