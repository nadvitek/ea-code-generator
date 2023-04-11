#!/bin/bash
set -e

echo "******************************************************"
echo "******           FIX JAVA INHERITANCE           ******"
echo "******************************************************"

sh "./sh/fix_java_inheritance.sh"

echo "******************************************************"
echo "******                 FIX JAVA                 ******"
echo "******************************************************"
java -cp target/classes cz/cez/cpr/eacodegenerator/core/util/fixer/JavaFixer

# Print directory structure as a tree
bash ./sh/print_directory.sh ./output_java/src
