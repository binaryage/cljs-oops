#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

./scripts/prepare-jar.sh
./scripts/list-jar.sh
lein with-profile lib install
