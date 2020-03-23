#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

lein with-profile +testing-basic-onone,+auto-testing cljsbuild auto basic-onone
