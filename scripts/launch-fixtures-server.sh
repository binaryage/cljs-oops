#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

cd "$DEV_FIXTURES_SERVER_ROOT"

echo "launching fixtures server for development (silent mode) in '$DEV_FIXTURES_SERVER_ROOT' on port $DEV_FIXTURES_SERVER_PORT"

python -m SimpleHTTPServer "$DEV_FIXTURES_SERVER_PORT" >/dev/null 2>&1
