#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

cd "$DEV_FIXTURES_SERVER_ROOT"

echo "launching fixtures server for development (silent mode) in '$DEV_FIXTURES_SERVER_ROOT'"

set -x
python -m http.server "$DEV_FIXTURES_SERVER_PORT" >/dev/null 2>&1
