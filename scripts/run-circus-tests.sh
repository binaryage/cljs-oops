#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

export OOPS_ELIDE_DEVTOOLS=1

if [[ ! -z "$1" ]]; then
  export OOPS_FT_FILTER="$1"
fi

lein with-profile +circus run -m "oops.circus"

popd
