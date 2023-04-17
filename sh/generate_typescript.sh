#!/bin/bash
set -e

npm config set offline false

# Re-generate server code
rm -rf output_typescript

# Generate TypeScript
# https://openapi-generator.tech/docs/generators/typescript-fetch/
java -jar openapi-generator-cli-5.0.1.jar batch ./export/ts-config.yaml

# Create TypeScript Project
rm -rf ./output_typescript/.openapi-generator
rm -rf ./output_typescript/.openapi-generator-ignore
rm -rf ./output_typescript/runtime.ts
cp ./export/package.json ./output_typescript/package.json

# remove: export * from './runtime';
sed -i 's|export \* from '\''\./runtime'\'';||g' ./output_typescript/index.ts

# remove function objectToJSON(...)
find ./output_typescript/apis -type f \( -name '*Api.ts' \) | xargs sed -i '
s/objectToJSON\((.*)\)/\1/
'

# Print directory structure as a tree
bash ./sh/print_directory.sh ./output_typescript

bash ./sh/fix_ts.sh
bash ./sh/fix_ts_date_nosubstring.sh

# temporary disable
# bash ./sh/fix_ts_array.sh
