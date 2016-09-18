#!/usr/bin/env bash

# updates all version strings

set -e

pushd `dirname "${BASH_SOURCE[0]}"` > /dev/null
source "./config.sh"

pushd "$ROOT"

# using https://github.com/ekalinin/github-markdown-toc.go
# brew install github-markdown-toc

gh-md-toc readme.md --depth 3

popd

popd
