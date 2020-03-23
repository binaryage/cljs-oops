#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

export OOPS_ELIDE_DEVTOOLS=1

if [[ -n "$1" ]]; then
  export OOPS_FT_FILTER="$1"
fi

./scripts/clean.sh
lein with-profile +circus run -m "oops.circus"
