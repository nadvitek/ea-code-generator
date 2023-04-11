#!/bin/bash
set -e

# Re-generate server code
rm -rf output_java

# Generate Java
# https://openapi-generator.tech/docs/generators/spring/
java -Xmx3000M -jar openapi-generator-cli-5.0.1.jar batch ./export/java-config.yaml

# Create Java Project
rm -rf ./output_java/.openapi-generator
rm -rf ./output_java/.openapi-generator-ignore
rm -rf ./output_java/README.md
cp ./export/pom.xml ./output_java/pom.xml

# Remove the body of class which contains $virtualProperty
find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i -z 's/private String $virtualProperty;.*}/}/g'
find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i 's/@JsonProperty("$virtualProperty")//g'

# Print directory structure as a tree
bash ./sh/print_directory.sh ./output_java
