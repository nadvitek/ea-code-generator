#!/bin/bash
set -e

# Template for debug
# sed -z 's/private String $virtualProperty;.*}/}/g' ./output_java/src/main/java/cz/cez/cpr/col/col_domain/v1_0/domain/AcademicTitle.java

find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i '
s/@javax.annotation.Generated(.*)//g;
s/@JsonProperty(.*)//g
s/@ApiModelProperty(.*)//g
s/@org.springframework.format.annotation.DateTimeFormat(.*)//g
s/@JsonSubTypes({[.*\n]*})//g
s/@JsonTypeInfo(.*)//g
s/@ApiModel(.*)//g
s/@NotNull//g
s/@Valid//g
s/@JsonCreator//g
s/@JsonValue//g
s/import com.*//g
s/import io.*//g
s/import javax.*//g
s/import org.*//g
s/org.springframework.core.io.Resource/byte\[\]/g
s/HackForClassNameCase/Case/g
s/import .*AllOf;//g
s/class \(.*\) {/class \1 implements java.io.Serializable { private static final long serialVersionUID = 1L;/
'

# Remove any:
# @JsonSubTypes({ ... non greedy multiline text ... })
find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i -z 's/@JsonSubTypes({[^}]*})//g'

# Remove the body of class which contains $virtualProperty
find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i -z 's/private String $virtualProperty;.*}/}/g'

# Remove all files *AllOf.java
find ./output_java/src -type f \( -name '*AllOf.java' \) -exec rm -rf {} \;

# Remove file ZRootComplexSchema.java
find ./output_java/src -type f \( -name 'ZRootComplexSchema.java' \) -exec rm -rf {} \;

# Remove folder with controller
controller=$(find ./output_java -name ApiUtil.java)
controller=$(dirname $controller)
echo Removing folder...
echo $controller
rm -rf $controller

# Rename file HackForClassNameCase.java to Case.java
case=$(find ./output_java -name HackForClassNameCase.java)
domain=$(dirname $case)
mv $domain/HackForClassNameCase.java $domain/Case.java

# Print directory structure as a tree
bash ./sh/print_directory.sh ./output_java
