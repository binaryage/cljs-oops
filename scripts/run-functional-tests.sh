#!/usr/bin/env bash

# checks if all version strings are consistent

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT/test/resources"

PHANTOM_VERSION=`phantomjs --version`
echo
echo "Running functional tests against PhantomJS $PHANTOM_VERSION..."
echo "===================================================================================================="
lein build-tests
phantomjs phantom.js "$@"

popd
