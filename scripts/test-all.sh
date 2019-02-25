#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

lein clean
./scripts/run-functional-tests-clojure18.sh

lein clean
./scripts/run-functional-tests-clojure19.sh

lein clean
./scripts/run-functional-tests.sh
./scripts/run-circus-tests.sh "$@"
