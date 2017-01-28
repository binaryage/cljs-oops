#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

mkdir -p "$EXPECTED_TRANSCRIPTS_DIR"

rsync -a "$ACTUAL_TRANSCRIPTS_DIR/" "$EXPECTED_TRANSCRIPTS_DIR"

popd

popd
