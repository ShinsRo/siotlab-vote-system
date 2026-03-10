#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

cd "${PROJECT_DIR}"

export DOCKER_BUILDKIT="${DOCKER_BUILDKIT:-1}"

docker build -f Dockerfile.base -t vote-base .
docker build -f Dockerfile.api -t vote-api .
docker build -f Dockerfile.adm -t vote-adm .
