#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

lein clean
./scripts/run-functional-tests-clojure18.sh
lein clean
./scripts/run-functional-tests.sh
./scripts/run-circus-tests.sh

popd
