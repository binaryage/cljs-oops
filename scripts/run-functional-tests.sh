#!/usr/bin/env bash

# checks if all version strings are consistent

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT/test/resources"

export OOPS_ELIDE_DEVTOOLS=1

PHANTOM_VERSION=`phantomjs --version`
echo
echo "Running functional tests under PhantomJS v$PHANTOM_VERSION"
echo "===================================================================================================="
lein build-tests
phantomjs phantom.js "$@"

popd
