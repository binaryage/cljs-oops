#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

if [[ -z "$1" ]]; then
  PROFILES=""
else
  PROFILES="$1,"
fi

lein with-profile "$PROFILES+testing-basic-onone" cljsbuild once basic-onone
lein with-profile "$PROFILES+testing-basic-oadvanced-core" cljsbuild once basic-oadvanced-core
lein with-profile "$PROFILES+testing-basic-oadvanced-goog" cljsbuild once basic-oadvanced-goog
