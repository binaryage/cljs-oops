#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

export OOPS_DISABLE_TEST_RUNNER_ANSI=1
lein with-profile +cooper,+dev-basic-onone cooper
