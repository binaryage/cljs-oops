#!/usr/bin/env bash

# checks if all version strings are consistent

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT/test/resources"

lein run-functional-tests
lein run-circus

popd
