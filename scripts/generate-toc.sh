#!/usr/bin/env bash

# updates all version strings

set -e -o pipefail

# shellcheck source=_config.sh
source "$(dirname "${BASH_SOURCE[0]}")/_config.sh"

cd "$ROOT"

# using https://github.com/ekalinin/github-markdown-toc.go
# brew install github-markdown-toc

gh-md-toc readme.md --depth 3
