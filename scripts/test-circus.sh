#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

export OOPS_ELIDE_DEVTOOLS=1

if [[ -n "$1" ]]; then
  export OOPS_FT_FILTER="$1"
fi

./scripts/clean.sh
lein with-profile +circus run -m "oops.circus"
