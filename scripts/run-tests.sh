#!/usr/bin/env bash

# checks if all version strings are consistent

set -e

. "$(dirname "${BASH_SOURCE[0]}")/config.sh"

pushd "$ROOT/test/resources"

phantomjs phantom.js "$@"

popd
