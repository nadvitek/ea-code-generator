#!/bin/bash
set -e

# The script fixs problems with date and timezones.
# E.g. When we want to convert the date 22.1.2021 00:00 +2 to the format ISO, the result is 2021-21-01T22:00Z. As you can see, when we cut the time, the result date is the day before 21.1.2021. Therefore it's the date formatted manually to the format "YYYY-mm-dd".

# Replace something like this in models/*.ts files:
#
# 'individualBirthDate: value.individualBirthDate === undefined ? undefined : (value.individualBirthDate).toISOString().substr(0,10);
#
# With:
#
# 'individualBirthDate: value.individualBirthDate == undefined ? undefined : `${value.\1.getFullYear()}-${(value.\1.getMonth() + 1).toString().padStart(2, "0")}-${value.\1.getDate().toString().padStart(2, "0")}`
#

find ./output_typescript/models -type f \( -name '*.ts' \) | xargs sed -i 's/'\''\([^ ]*\)'\'': value.\(.*\) === undefined ? undefined : (value.\(.*\).toISOString().substr(0,10))/'\''\1'\'': value.\1 == undefined ? undefined : `${value.\1.getFullYear()}-${(value.\1.getMonth() + 1).toString().padStart(2, "0")}-${value.\1.getDate().toString().padStart(2, "0")}`/g'



# Replace something like this in api/*Api.ts files:
#
# queryParameters['individualBirthDate'] = (requestParameters.individualBirthDate as any).toISOString().substr(0,10);
#
# With:
#
# const requestValueDate: Date | null = requestParameters.individualBirthDate;
# queryParameters['individualBirthDate'] = (requestValueDate == null ? null : `${requestValueDate.getFullYear()}-${(requestValueDate.getMonth() + 1).toString().padStart(2, "0")}-${requestValueDate.getDate().toString().padStart(2, "0")}`);
# date is in the format YYYY-mm-dd

find ./output_typescript/apis -type f \( -name '*Api.ts' \) | xargs sed -i 's/queryParameters\['\''\([^ ]*\)'\''\] = (requestParameters.\(.*\) as any).toISOString().substr(0,10);/const requestValueDate: Date | null = requestParameters.\1;  queryParameters\['\''\1'\''\] = (requestValueDate == null ? null : `${requestValueDate.getFullYear()}-${(requestValueDate.getMonth() + 1).toString().padStart(2, "0")}-${requestValueDate.getDate().toString().padStart(2, "0")}`);/'

