#!/bin/bash
set -e

echo "******************************************************"
echo "******              FIX TYPESCRIPT              ******"
echo "******************************************************"
java -cp target/classes cz/cez/cpr/eacodegenerator/core/util/fixer/TypeScriptFixer

# Print directory structure as a tree
bash ./sh/print_directory.sh ./output_typescript
