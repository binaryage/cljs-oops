#!/usr/bin/env bash

set -e -o pipefail

pushd() {
  command pushd "$@" >/dev/null
}

popd() {
  command popd >/dev/null
}

pushd .

cd "$(dirname "${BASH_SOURCE[0]}")/.."

ROOT=$(pwd)
PROJECT_VERSION_FILE="src/lib/oops/version.clj"
PROJECT_FILE="project.clj"
DEV_FIXTURES_SERVER_ROOT="$ROOT/test/resources"
DEV_FIXTURES_SERVER_PORT=7119
EXPECTED_TRANSCRIPTS_DIR="$ROOT/test/transcripts/expected"
ACTUAL_TRANSCRIPTS_DIR="$ROOT/test/transcripts/_actual_"

popd
