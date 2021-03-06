#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

export OOPS_ELIDE_DEVTOOLS=1

echo
echo "Testing (functional) under Puppeteer, using Clojure 1.10"
echo "===================================================================================================="
./scripts/clean.sh
./scripts/build-tests.sh
./scripts/run-functional-tests.sh
