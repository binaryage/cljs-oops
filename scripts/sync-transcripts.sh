#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

mkdir -p "$EXPECTED_TRANSCRIPTS_DIR"

rsync -a "$ACTUAL_TRANSCRIPTS_DIR/" "$EXPECTED_TRANSCRIPTS_DIR"
