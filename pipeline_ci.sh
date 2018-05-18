#!/bin/bash

SERVICE="weather"

## Options
NO_CACHE=
ENABLE_BUILD="true"
ENABLE_DEPLOY="true"
DAEMON_MODO=false


if [ ! -f ./pipeline_ci.sh ]; then
    echo "This script should be run on the same directory it resides."
    exit 1
fi

function pipeline_help() {
    echo "Options:"
    echo "  --no-cache       Build docker file using --no-cache option."
    echo "  --build-only     Do not deploy the application."
    echo "  --deploy-only    Do not build the application."
    echo "  --daemon         Deploy docker image in background."
    echo "  --help|-h"
}

for arg in "$@"; do
    case "$arg" in
    --no-cache)
        NO_CACHE="--no-cache"
        ;;
    --build-only)
        ENABLE_DEPLOY="false"
        ;;
    --deploy-only)
        ENABLE_BUILD="false"
        ;;
    --daemon)
        DAEMON_MODO=true
        ;;
    --help|-h)
        pipeline_help
        exit 0
        ;;
    esac
done

# Build
function build() {
    echo "**** Starting build process"

    [[ -z ${NO_CACHE} ]] && echo "No cache build enabled"
    docker build ${NO_CACHE} -t ${SERVICE} -f Dockerfile-ci .
    if [[ $? -ne 0 ]]; then
        echo "[ERROR] fail building docker image. Exiting..."
        exit 1
    fi

    echo "**** Finished build process"
}

# Deploy
function deploy() {
    echo "**** deploying locally application"
    LOCAL_PORT=8080

    local options=
    if [[ ${DAEMON_MODO} = true ]]; then
        echo "daemon deploy enabled"
        option="-d"
    fi

    docker run ${option} -p ${LOCAL_PORT}:8080 ${SERVICE}
}

function run_pipeline() {
    echo "---- Starting Pipeline ----"
    echo ""

    VERSION=$(git rev-parse --short HEAD)
    echo "building git short hash = ${VERSION}"

    [[ ${ENABLE_BUILD} != "true" ]] && echo "Skipping build..." || build
    [[ ${ENABLE_DEPLOY} != "true" ]] && echo "Skipping deploy..." || deploy
}

run_pipeline ${@}