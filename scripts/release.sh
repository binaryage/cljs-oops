#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

lein clean
./scripts/check-versions.sh
./scripts/prepare-jar.sh
./scripts/check-release.sh
./scripts/deploy-clojars.sh
