#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

export OOPS_DISABLE_TEST_RUNNER_ANSI=1
lein with-profile +cooper,+dev-basic-onone cooper
