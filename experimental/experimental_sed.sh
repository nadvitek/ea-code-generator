#!/bin/bash
set -e

# sed -z 's/class \(.*\) {/class \1 implements Serializable { private static final long serialVersionUID = 1L;/' ./experimental/Test.java

# sed -z 's/queryParameters\['\''\([^ ]*\)'\''\] = (requestParameters.\(.*\) as any).toISOString().substr(0,10);/aaaa \1 cccc/' ./experimental/CPRCOLUSERApi.ts | less

# sed -z 's/queryParameters\['\''\([^ ]*\)'\''\] = (requestParameters.\(.*\) as any).toISOString().substr(0,10);/aaaa let ymd: Date = requestParameters.\1; if (ymd === null) { queryParameters\['\''\1'\''\] = null; } else { queryParameters\['\''\1'\''\] =  \[ymd.getFullYear(),ymd.getMonth(),ymd.getDay()\]; }/' ./experimental/CPRCOLUSERApi.ts | less

# sed -i 's/'\''\(.*\)'\'': ((json\['\''\(.*\)'\''\] as Array<any>).map(\(.*\)))/\1 - \2 - \3/' ./experimental/UserMergeDto.ts

# 'contacts': (json['contacts'] === null ? null : (json['contacts'] as Array<any>).map(ContactDtoFromJSON)),
sed -i 's/'\''\(.*\)'\'': ((json\['\''\(.*\)'\''\] as Array<any>).map(\(.*\)))/'\''\1'\'': (json['\''\1'\''] === null ? null : (json['\''\1'\''] as Array<any>).map(\3))/' ./experimental/UserMergeDto.ts

# let ymd: Date = requestParameters.individualBirthDate; if (ymd === null) { queryParameters['individualBirthDate'] = null; } else { queryParameters['individualBirthDate'] =  [ymd.getFullYear(),ymd.getMonth(),ymd.getDay()]; }
