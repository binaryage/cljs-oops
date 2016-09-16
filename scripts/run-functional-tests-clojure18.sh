#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT/test/resources"

export OOPS_ELIDE_DEVTOOLS=1

PHANTOM_VERSION=`phantomjs --version`
echo
echo "Running functional tests under PhantomJS v$PHANTOM_VERSION, using Clojure 1.8"
echo "===================================================================================================="
lein with-profile +clojure18 build-tests
phantomjs phantom.js "$@"

popd
