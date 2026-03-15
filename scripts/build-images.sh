#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
TARGET="${1:-all}"

build_vote_service() {
  cd "${PROJECT_DIR}/vote-service"
  export DOCKER_BUILDKIT="${DOCKER_BUILDKIT:-1}"

  docker build -f Dockerfile.base -t vote-base .
  docker build -f Dockerfile.api -t vote-api .
  docker build -f Dockerfile.adm -t vote-adm .
}

build_vote_gateway() {
  cd "${PROJECT_DIR}/vote-gateway"
  export DOCKER_BUILDKIT="${DOCKER_BUILDKIT:-1}"

  docker build -f Dockerfile.base -t vote-gateway-base .
  docker build -f Dockerfile -t vote-gateway .
}

case "${TARGET}" in
  all)
    build_vote_service
    build_vote_gateway
    ;;
  service)
    build_vote_service
    ;;
  gateway)
    build_vote_gateway
    ;;
  *)
    echo "usage: ./scripts/build-images.sh [all|service|gateway]" >&2
    exit 1
    ;;
esac
