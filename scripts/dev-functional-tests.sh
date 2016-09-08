#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

export OOPS_DISABLE_TEST_RUNNER_ANSI=1
lein with-profile +cooper,+dev-basic-onone cooper

popd

popd
