#!/bin/bash
set -e

npm cache clean --force
npm install --
npm run build
cp ./package.json ./dist/package.json

# Replace: "dist/" with "./"
sed -i 's|dist/|./|g' ./dist/package.json

# Print directory structure as a tree
bash ../sh/print_directory.sh ./dist
