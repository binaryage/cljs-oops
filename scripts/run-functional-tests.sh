#!/usr/bin/env bash

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

cd "$ROOT"

set -x
node "test/resources/puppeteer.js" "test/resources" "main.html?script=basic_onone"
node "test/resources/puppeteer.js" "test/resources" "main.html?script=basic_oadvanced_core"
node "test/resources/puppeteer.js" "test/resources" "main.html?script=basic_oadvanced_goog"
