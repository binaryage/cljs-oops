#!/usr/bin/env bash

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

if [ ! -d "$OOPS_BROWSER_TESTS_USER_PROFILE" ] ; then
  mkdir -p "$OOPS_BROWSER_TESTS_USER_PROFILE"
fi

EXE="/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary"
if [ -f /usr/bin/google-chrome-unstable ] ; then
  EXE="/usr/bin/google-chrome-unstable" # this is for ubuntu
fi
if [ -n "$OOPS_USE_CHROME" ] ; then
  EXE="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
fi
if [ -n "$OOPS_USE_CHROMIUM" ] ; then
  EXE="/Applications/Chromium.app/Contents/MacOS/Chromium"
fi
if [ -n "$OOPS_USE_CUSTOM_CHROME" ] ; then
  EXE="$OOPS_USE_CUSTOM_CHROME"
fi

echo "selected browser binary '$EXE'"

set -x
"$EXE" \
      --remote-debugging-port=${OOPS_CHROME_REMOTE_DEBUGGING_PORT:=9222} \
      --user-data-dir="$OOPS_BROWSER_TESTS_USER_PROFILE" \
      --no-first-run \
      --enable-experimental-extension-apis \
      --disk-cache-dir=/dev/null \
      --media-cache-dir=/dev/null \
      --disable-hang-monitor \
      --disable-prompt-on-repost \
      --dom-automation \
      --full-memory-crash-report \
      --no-default-browser-check \
      --disable-gpu \
      "http://localhost:$DEV_FIXTURES_SERVER_PORT/main.html?script=basic_onone" 2> /dev/null
set +x

popd

popd
