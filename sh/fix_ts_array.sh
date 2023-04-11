#!/bin/bash
set -e

# The script fixs problems with array.
#
# Replace something like this in models/*.ts files:
#
# 'contacts': ((json['contacts'] as Array<any>).map(ContactDtoFromJSON)),
#
# With:
#
# 'contacts': (json['contacts'] === null ? null : (json['contacts'] as Array<any>).map(ContactDtoFromJSON)),
#

find ./output_typescript/models -type f \( -name '*.ts' \) | xargs sed -i 's/'\''\(.*\)'\'': ((json\['\''\(.*\)'\''\] as Array<any>).map(\(.*\)))/'\''\1'\'': (json['\''\1'\''] === null ? null : (json['\''\1'\''] as Array<any>).map(\3))/g'
