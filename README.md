# EA Code Generator

# Použití aplikace
## Tvorba Modelu
První krok pro 
použití aplikace je vytvoření modelu v Enterprise Architect. Model je vytvořen v balíčku se jménem rozhraní, který v sobě obsahuje další balíčky s číslem verze API. Např. Library_System/1.0.4/"model"
Pokud však uživatel chce generovat pouze LDM, tak stačí model uložit pouze do určitého balíčku.
Pro správné fungování aplikace je důležité, aby uživatel nastavoval objektům v modelu tzv. Stereotypy. Jejich pojmenování musí být přesné, aby aplikace poznala, o jaký typ objektu se jedná.
Pro vytvoření diagramů používá uživatel vždy diagram typu UML Structural - Class.

### Tvorba Child Diagramu
Na začátek je důležité vysvětlit, jak zabalit jednotlivé diagramy pod objekty kvůli větší přehlednosti a strukturalizaci. Značka, která říká, že daný objekt obsahuje v sobě diagram, se vyznačuje dvěma elipsami spojenýma čarou vpravo dole viz Obrázek. Přiřazení tzv. child diagramu objektu je možné pomocí kliknutí pravého tlačítka myši na daný objekt, přejít na sekci "New Child Diagram" a zvolit Add Diagram. Tímto krokem se diagram přímo vytvoří a zároveň propojí s daným objektem. Přejití na diagram je poté možné pomocí dvojího kliku myši na daný objekt obsahující diagram.

![child_diagram.png](src%2Fmain%2Fresources%2Fdocumentation%2Fchild_diagram.png)

### Schéma
V modelaci schématu nebudou potřeba žádné Stereotypy. Všechny třídy budou vytvořeny pomocí objektu typu Class a všechny jejich atributy budou objekty typu Primitive, které budou mít napřímo jeden atribut. Atribut se jménem “type” s typem podle toho, o jaký typ atributu se jedná. Atributy budou na třídy přímo navázané pomocí vazby Compose viz Obrázek. Závislost tříd je vytvořená také pomocí vazby Compose. Pokud chcete vytvořit oboustrannou závislost, použijte vazbu Compose dvakrát jedním i druhým směrem.

![schema.png](src%2Fmain%2Fresources%2Fdocumentation%2Fschema.png)

### Interface
Interface je základ, pokud uživatel usiluje o generování API. Interface je objekt typu Interface. Pojmenování rozhraní a jeho popis budou promítnuty do nadpisu a popisu výsledného Swaggeru. Důležité je označit Interface Stereotypem “ApiInterface” viz Obrázek.

![interface.png](src%2Fmain%2Fresources%2Fdocumentation%2Finterface.png)
### Metody
Metody mohou být pěti druhů a to GET, PUT, POST, DELETE nebo PATCH. Podle toho, jaký druh metody chceme použít, určíme objektu Stereotyp. Například v případě metody typu GET bychom použili Stereotyp “GetMethod”. Všechny metody jsou objekty typu Class a jsou na Interface navázány vazbou Compose, která má Stereotyp “ApiPath” a je pojmenovaná podle URL cesty endpointu metody viz Obrázek.

![methods.png](src%2Fmain%2Fresources%2Fdocumentation%2Fmethods.png)

### Request
Na request neboli požadavek jsou navázány vstupy vytvořené metody, které je potřeba zadat. Všechny vstupy navazujeme na request přímo a to pomocí vazby Compose, která má vždy Stereotyp podle toho, o jaký druh vstupu se jedná. Existují tři druhy Stereotypů a tedy vstupů, které uživatel může použít:

<ol>
<li><b>Path Variable</b> - jde o vstup primitivního typu, který slouží jako argument v endpointu. Například měli bychom metodu Get na endpoint /users/\{id\}, na metodu musí být navázán objekt typu Primitive, jako bylo použito ve schématu v 5.1.2. Pokud použijeme endpoint, který požaduje parametr a nezadáme ho do modelu, bude se jednat o neplatný model. Vazba, která půjde z primitivního typu do requestu musí mít Stereotyp ParameterInPath a být typu Compose viz Obrázek.

![path_var.png](src%2Fmain%2Fresources%2Fdocumentation%2Fpath_var.png)
<li><b>Request Body</b> - jde o vstup objektového typu. Pravděpodobně se jedná o třídu, kterou už máme vytvořenou ve schématu. Nevytvářejte novou třídu, jinak by mohla nastat duplikace tříd. Nakopírujte třídu z vašeho schématu jako link. Vazba, která jde z objektu do requestu, musí mít Stereotyp ParameterInBody a být typu Compose viz Obrázek.</li>

![par_body.png](src%2Fmain%2Fresources%2Fdocumentation%2Fpar_body.png)
<li><b>Query Parameter</b> - jde o typ, který je užitečný jako parametr v SQL dotazech. Vazba, která jde z dotazového parametru do requestu, musí mít Stereotyp ParameterInQuery a být typu Compose viz Obrázek.</li>

![par_query.png](src%2Fmain%2Fresources%2Fdocumentation%2Fpar_query.png)
</ol>



### Response
Response neboli také jinak nazýváno odpověď. Může nastat více různých odpovědí s různými úspěchy a neúspěchy nabývajících různých HTTP status kódů, proto je nutné vytvořit rodičovskou odpověď typu Class, kterou pojmenujeme Response. Od té pak budou dědit všechny odpovědi, které mohou nastat v metodě pomocí vazby Generalize. Každá rodičovská Response by měla obsahovat alespoň Response 200 a Response 400 viz Obrázek. Aplikace je schopna zpracovat Response kódu 200, 204 a kódů 400-405.

Jak rodičovská třída, tak dědicové této třídy, musí obsahovat Stereotyp Response. Podle toho, jaký HTTP status kód vrací response, pojmenujeme danou třídu. Například pro úspěch bychom třídu dědící ze třídy Response pojmenovali Response 200.

![response1.png](src%2Fmain%2Fresources%2Fdocumentation%2Fresponse1.png)

Na odpovědi, které vracejí známé chybové hlášky, není potřeba navazovat cokoliv dalšího. Aplikace sama ví, jaké kódy vracejí jaké hlášky. Na odpovědi, které jsou úspěšné a vracejí jeden nebo více různých objektů, je potřeba objekty navázat. To pomocí nalinkování objektu ze schématu. Navázat je potřeba objekty na jednotlivé odpovědi vazbou typu Compose viz Obrázek.

![response2.png](src%2Fmain%2Fresources%2Fdocumentation%2Fresponse2.png)

### Databázový server
Výše vytvořený model je potřeba mít napojený na nastavený MySQL server, který si uživatel musí nainstalovat a napojit na svůj model v Enterprise Architect. K databázi bude připojena i aplikace.\cite{MySQLServer}


## Použití aplikace
Pro použití aplikace je nutné nastavit parametry v konfiguraci, podle kterých aplikace pozná, zda se jedná o LDM nebo o API a další parametry. Vše se nastavuje v souboru
src/main/resources/application.properties.
### Databáze
Pro nastavení databáze je nutné nastavit parametry sekce spring.datasource.
Databázové properties:
<ul>
<li>spring.datasource.url - URL MySQL serveru</li>
<li>spring.datasource.username - přihlašovací jméno k MySQL serveru</li>
<li>spring.datasource.password - přihlašovací heslo k MySQL serveru</li>
</ul>


### Typ generování
<ul>
<li>API - pro typ generování API nastavíme proměnné sekce ea.interface. Mezi ně patří jméno rozhraní a verze balíčku, ve kterém se nachází tak, jak je popsáno v 5.1. API properties:</li>
<ul>
<li>ea.interface.name - název rozhraní</li>
<li>ea.interface.version.main - hlavní verze balíčku, ve kterém se nachází model</li>
</ul>
<li>LDM - pro typ generování LDM je nutné nastavit pouze absolutní cestu balíčku, ve kterém se nachází vymodelované schéma a to v kódování base64. Pro kódování použijte některý internetový kodér. Pro generování typu LDM existují také další properties, které lze nastavit.</li>
<ul>
<li>ea.ldm.packageBase64 - absolutní cesta k balíčku se schématem zakódovaná v Base64</li>
<li>ea.ldm.version.main - hlavní verze</li>
<li>ea.ldm.version.minor - vedlejší verze</li>
<li>ea.ldm.descriptionBase64 - popis zakódovaný v Base64</li>
</ul>
</ul>

### Parametry výstupu
Nyní už je potřeba nastavit pouze parametry výstupu generování stubů. Podle toho, v jakém jazyce či frameworku chcete mít vygenerované Frontend a Backend stuby, nastavte proměnné typu generation.language. Všechny možnosti naleznete na stránkách OpenAPI generátoru\cite{GenList}. Properties pro generování:
<ul>
<li>generation.language.frontend - název nástroje pro generování Frontendu</li>
<li>generation.language.backend - název nástroje pro generování Backendu</li>
</ul>

Před spuštěním generování je také důležité si nainstalovat OpenAPI Generator. Pro jeho instalaci použijte instalační balíček npm. Návod na instalaci naleznete na github repositáři OpenAPI Generatoru\cite{Swaggen}. Vygenerovaný Swagger najdete po dokončení generování ve složce export. Vygenerované stuby naleznete ve složce gen.

Pokud generování selže, zkuste zkontrolovat, zda je Swagger korektní ve SwaggerEditoru a potom zkuste vygenerovat manuálně.

````shell script
npx @openapitools/openapi-generator-cli generate -i /export/swagger.yaml -g ${language_or_framework} -o /gen/${name_of_your_application}
````