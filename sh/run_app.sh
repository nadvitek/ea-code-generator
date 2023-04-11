#!/bin/bash
set -e

ldmPackage=$basePackage
ldmIgnoredPackage=$ignoredPackage
ldmGroupId=$groupId
ldmArtifactId=$artifactId
ldmMainVersion=$mainVersion
ldmMinorVersion=$minorVersion
ldmDescription=$description
logDetail=$logDetail

#basePackage="čez ict services/probíhající aktivity (to-be)/obchod b2c - čpr/07 programy/digitalizace/01 společná část/04 information layer/datový model/02 ldm (logický datový model)"
ldmPackageBase64=$(echo $ldmPackage | base64 -w 0)
ldmIgnoredPackageBase64=$(echo $ldmIgnoredPackage | base64 -w 0)
ldmDescriptionBase64=$(echo $ldmDescription | base64 -w 0)
echo ldmPackageBase64: $ldmPackageBase64
echo ldmIgnoredPackageBase64: $ldmIgnoredPackageBase64
echo ldmDescriptionBase64: $ldmDescriptionBase64

java -jar -Dspring.profiles.active=prod target/ea-code-generator-0.0.1-SNAPSHOT.jar \
	cz.cez.cpr.eacodegenerator.core.EaCodeGeneratorApplication \
	--ea.interface.name=${interfaceName} \
	--ea.interface.version.main=${interfaceVersion} \
	--ea.log.detail=${logDetail} \
	--ea.ldm.packageBase64=${ldmPackageBase64} \
	--ea.ldm.ignoredPackageBase64=${ldmIgnoredPackageBase64} \
	--ea.ldm.groupId=${ldmGroupId} \
	--ea.ldm.artifactId=${ldmArtifactId} \
	--ea.ldm.version.main=${ldmMainVersion} \
	--ea.ldm.version.minor=${ldmMinorVersion} \
	--ea.ldm.descriptionBase64=${ldmDescriptionBase64} \
	--spring.datasource.username=${dbUser} \
	--spring.datasource.password=${dbPass}

chmod +x ./export/*.sh
ls ./export

# Remove comments in swagger
# sed "s/^[ \t]*#.*//" -i ./export/swagger.yaml
# sed "/^$/d" -i ./export/swagger.yaml
