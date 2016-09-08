#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

echo "launching dirac agent with repl"

lein with-profile +repl-with-agent repl :headless

popd

popd
