#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

cd "$ROOT"

export OOPS_ELIDE_DEVTOOLS=1

echo
echo "Testing (functional) under Puppeteer, using Clojure 1.10"
echo "===================================================================================================="
./scripts/clean.sh
./scripts/build-tests.sh
./scripts/run-functional-tests.sh
