#!/bin/bash
set -e

# Print directory structure as a tree
echo "Directory: $1"
find "$1" | sed -e "s/[^-][^\/]*\//  |/g" -e "s/|\([^ ]\)/|-\1/"