#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

echo "launching dirac agent with repl"

lein with-profile +repl-with-agent repl :headless
