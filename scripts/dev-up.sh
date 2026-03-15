#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
MODE="${1:-gateway}"

cd "${PROJECT_DIR}"

case "${MODE}" in
  gateway)
    docker compose -f docker/compose.yaml --profile gateway up -d mysql vote-api vote-adm
    ;;
  service)
    docker compose -f docker/compose.yaml up -d mysql
    ;;
  *)
    echo "usage: ./scripts/dev-up.sh [gateway|service]" >&2
    exit 1
    ;;
esac
