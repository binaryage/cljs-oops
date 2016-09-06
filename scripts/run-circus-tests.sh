#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

export OOPS_ELIDE_DEVTOOLS=1

lein with-profile +circus run -m "oops.circus"

popd
