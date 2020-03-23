#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

set -x
./scripts/test-functional-clojure110.sh
./scripts/test-functional-clojure19.sh
./scripts/test-functional-clojure18.sh
./scripts/test-circus.sh "$@"
