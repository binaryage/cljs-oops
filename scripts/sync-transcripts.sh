#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

mkdir -p "$EXPECTED_TRANSCRIPTS_DIR"

rsync -a "$ACTUAL_TRANSCRIPTS_DIR/" "$EXPECTED_TRANSCRIPTS_DIR"
