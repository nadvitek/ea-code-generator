# EA Code Generator

Run on localhost:

````shell script
VM options: -Dspring.profiles.active=local
````


If you want to generate the application from
generated swagger, first install openapi-generator:

````shell script
npm install @openapitools/openapi-generator-cli -g
````

If the generation fails, then generate manually:

````shell script
npx @openapitools/openapi-generator-cli generate -i /export/swagger.yaml -g ${language_or_framework} -o /gen/${name_of_your_application}
````
