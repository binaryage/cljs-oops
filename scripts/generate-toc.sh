#!/usr/bin/env bash

# updates all version strings

set -e

cd `dirname "${BASH_SOURCE[0]}"` && source "./config.sh" && cd "$ROOT"

# using https://github.com/ekalinin/github-markdown-toc.go
# brew install github-markdown-toc

gh-md-toc readme.md --depth 3
