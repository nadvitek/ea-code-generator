#!/bin/bash
set -e

actual="./export/swagger.yaml"
expected="./src/test/resources/expected.yaml"

diff -c $actual $expected

if cmp --silent -- "$actual" "$expected"; then
  echo "files contents are identical"
else
  echo "files differ"
  exit 64
fi
