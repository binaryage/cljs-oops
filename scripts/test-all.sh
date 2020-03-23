#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

set -x
./scripts/test-functional-clojure110.sh
./scripts/test-functional-clojure19.sh
./scripts/test-functional-clojure18.sh
./scripts/test-circus.sh "$@"
