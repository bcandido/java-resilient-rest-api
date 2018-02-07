#!/bin/bash

SERVICE="weather"

## Options --no-cache
NO_CACHE=

if [ ! -f ./pipeline_ci.sh ]; then
    echo "This script should be run on the same directory it resides."
    exit 1
fi

for arg in "$@"; do
    case "$arg" in
    --no-cache)
        echo "No cache build enabled"
        NO_CACHE="--no-cache"
        ;;
    esac
done

# Build
echo "**********************************"
echo "**** Starting build prococess ****"
echo "**********************************"
docker build ${NO_CACHE} -t ${SERVICE} .

# Deploy
echo "***************************************"
echo "**** deploying locally application ****"
echo "***************************************"
LOCAL_PORT=8080
docker run --restart always -p ${LOCAL_PORT}:8080 ${SERVICE}