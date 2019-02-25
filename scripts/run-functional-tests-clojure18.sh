#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

cd "$ROOT/test/resources"

export OOPS_ELIDE_DEVTOOLS=1

PHANTOM_VERSION=`phantomjs --version`
echo
echo "Running functional tests under PhantomJS v$PHANTOM_VERSION, using Clojure 1.8"
echo "===================================================================================================="
${ROOT}/scripts/build-tests.sh +clojure18
phantomjs phantom.js "$@"
