package cz.cez.cpr.eacodegenerator.core;

import cz.cez.cpr.eacodegenerator.core.configuration.LDMConfiguration;
import cz.cez.cpr.eacodegenerator.core.configuration.OpenApiConfiguration;
import cz.cez.cpr.eacodegenerator.core.generator.SwaggerGenerator;
import cz.cez.cpr.eacodegenerator.core.util.validations.Validations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Base64;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "cz.cez.cpr.eacodegenerator.core.repository")
@EntityScan("cz.cez.cpr.eacodegenerator.core.metamodel")
public class EaCodeGeneratorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(EaCodeGeneratorApplication.class);

	@Value("${ea.interface.name:#{null}}")
	private String interfaceName;

	@Value("${ea.interface.version.main:#{null}}")
	private String interfaceMainVersion;

	@Value("${ea.ldm.packageBase64:#{null}}")
	private String ldmPackageBase64;

	@Value("${ea.ldm.ignoredPackageBase64:#{null}}")
	private String ldmIgnoredPackageBase64;

	@Value("${ea.ldm.groupId:#{null}}")
	private String ldmGroupId;

	@Value("${ea.ldm.artifactId:#{null}}")
	private String ldmArtifactId;

	@Value("${ea.ldm.version.main:#{null}}")
	private String ldmMainVersion;

	@Value("${ea.ldm.version.minor:#{null}}")
	private String ldmMinorVersion;

	@Value("${ea.ldm.descriptionBase64:#{null}}")
	private String ldmDescriptionBase64;

	@Value("${generation.input:#{null}}")
	private String generationInput;

	@Value("${generation.output:#{null}}")
	private String generationOutput;

	@Value("${generation.language:#{null}}")
	private String generationLanguage;

	@Value("${generation.command.local:#{null}}")
	private String generationCommand;

	private final SwaggerGenerator generator;

	public EaCodeGeneratorApplication(SwaggerGenerator generator) {
		this.generator = generator;
	}

	public static void main(String[] args) {
		SpringApplication.run(EaCodeGeneratorApplication.class, args);
	}

	@PostConstruct
	public void init() {
		interfaceName = normalizeString(interfaceName);
		interfaceMainVersion = normalizeString(interfaceMainVersion);
		ldmPackageBase64 = normalizeString(ldmPackageBase64);
		ldmIgnoredPackageBase64 = normalizeString(ldmIgnoredPackageBase64);
		ldmGroupId = normalizeString(ldmGroupId);
		ldmArtifactId = normalizeString(ldmArtifactId);
		ldmMainVersion = normalizeString(ldmMainVersion);
		ldmMinorVersion = normalizeString(ldmMinorVersion);
		ldmDescriptionBase64 = normalizeString(ldmDescriptionBase64);

		printProperty("interfaceName:           ", interfaceName);
		printProperty("interfaceMainVersion:    ", interfaceMainVersion);
		printProperty("ldmPackageBase64:        ", ldmPackageBase64);
		printProperty("ldmIgnoredPackageBase64: ", ldmIgnoredPackageBase64);
		printProperty("ldmGroupId:              ", ldmGroupId);
		printProperty("ldmArtifactId:           ", ldmArtifactId);
		printProperty("ldmMainVersion:          ", ldmMainVersion);
		printProperty("ldmMinorVersion:         ", ldmMinorVersion);
		printProperty("ldmDescription:          ", ldmDescriptionBase64);
	}

	@Override
	public void run(String... args) {
		Validations.printRegistry();
		if (StringUtils.hasText(interfaceName) && StringUtils.hasText(interfaceMainVersion)) {
			generator.generate(new OpenApiConfiguration(interfaceName, interfaceMainVersion));
		} else if (StringUtils.hasText(ldmPackageBase64)) {
			Validations.muteValidations();
			generator.generate(new LDMConfiguration()
					.basePackage(decode(ldmPackageBase64))
					.ignoredPackage(decode(ldmIgnoredPackageBase64))
					.groupId(ldmGroupId)
					.artifactId(ldmArtifactId)
					.mainVersion(ldmMainVersion)
					.minorVersion(ldmMinorVersion)
					.description(decode(ldmDescriptionBase64))
			);
		} else {
			throw new UnsupportedOperationException("Unsupported input configuration!");
		}
		log.info("OpenAPI generation started!");
		//generateOpenApi();
		log.info("Done!");
	}

	private void printProperty(String name, String value) {
		log.info("{}{}", name, StringUtils.hasText(value) ? value : "value is empty");
	}

	private String normalizeString(String value) {
		return StringUtils.hasText(value) && !"null".equals(value) ? value : null;
	}

	private String decode(String text) {
		return text == null ? null :
				StringUtils.trimTrailingWhitespace(
					StringUtils.trimLeadingWhitespace(
						new String(Base64.getDecoder().decode(text))
					)
				);
	}

	private void generateOpenApi() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String command = String.format(generationCommand, generationInput, generationLanguage, generationOutput);
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			processBuilder.command("cmd.exe", "/c", command);
		} else {
			processBuilder.command("sh", "-c", command);
		}
		try {
			Process process = processBuilder.start();

			int exitCode = process.waitFor();

			if (exitCode != 0) {
				throw new RuntimeException();
			}

			log.info("OpenAPI generation finished!");
		} catch (Exception e) {
			log.warn("OpenAPI generation failed!", e);
		}
	}
}
