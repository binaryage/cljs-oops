#!/usr/bin/env bash

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

set -x
node "test/resources/puppeteer.js" "test/resources" "main.html?script=basic_onone"
node "test/resources/puppeteer.js" "test/resources" "main.html?script=basic_oadvanced_core"
node "test/resources/puppeteer.js" "test/resources" "main.html?script=basic_oadvanced_goog"
