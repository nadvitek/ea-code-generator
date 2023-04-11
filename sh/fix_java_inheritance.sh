#!/bin/bash
set -e

# Replace something like this:
#
# @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "codebookEntry", visible = true)
# @JsonSubTypes({
#   @JsonSubTypes.Type(value = CodebookEntryHierarchyDto.class, name = "CodebookEntryHierarchy"),
#   @JsonSubTypes.Type(value = CodebookEntryHierarchyDto.class, name = "codebookEntryHierarchy"),
# })
#
# With:
#
# @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, defaultImpl = CodebookEntryDto.class)
# @JsonSubTypes({
#   @JsonSubTypes.Type(value = CodebookEntryHierarchyDto.class),
#   @JsonSubTypes.Type(value = CodebookEntryHierarchyDto.class),
# })

find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i \
's/@JsonSubTypes.Type(value = \([^ ]*\),.*/@JsonSubTypes.Type(value = \1),/g'

find ./output_java/src -type f \( -name '*.java' \) | xargs -I FILE sed -i \
"s|@JsonTypeInfo(.*)|@JsonTypeInfo(FILE)|g" FILE > FILE;

find ./output_java/src -type f \( -name '*.java' \) | xargs sed -i \
's/@JsonTypeInfo(.*\/\(.*\).java)/@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, defaultImpl = \1.class)/g'
