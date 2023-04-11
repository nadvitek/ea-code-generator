#!/bin/bash
set -e

find ./experimental -type f \( -name '*.java' \) | xargs -I FILE sed -i "s|@JsonTypeInfo(.*)|@JsonTypeInfo(FILE)|g" FILE > FILE;
find ./experimental -type f \( -name '*.java' \) | xargs sed -i \
's/@JsonTypeInfo(.*\/\(.*\).java)/@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, defaultImpl = \1.class)/g'
