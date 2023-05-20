package cz.cvut.fel;

import cz.cvut.fel.configuration.LDMConfiguration;
import cz.cvut.fel.configuration.OpenApiConfiguration;
import cz.cvut.fel.generator.SwaggerGenerator;
import cz.cvut.fel.util.validations.Validations;
import lombok.Setter;
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
import java.io.IOException;
import java.util.Base64;

/**
 * Launching class representing EA
 * code generator
 */
@Setter
@SpringBootApplication
@EnableJpaRepositories(basePackages = "cz.cvut.fel.repository")
@EntityScan("cz.cvut.fel.metamodel")
public class EaCodeGeneratorApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(EaCodeGeneratorApplication.class);

	@Value("${ea.interface.name:#{null}}")
	private String interfaceName;

	@Value("${ea.interface.version.main:#{null}}")
	private String interfaceMainVersion;

	@Value("${ea.ldm.package:#{null}}")
	private String ldmPackage;

	@Value("${ea.ldm.ignoredPackage:#{null}}")
	private String ldmIgnoredPackage;

	@Value("${ea.ldm.version.main:#{null}}")
	private String ldmMainVersion;

	@Value("${ea.ldm.version.minor:#{null}}")
	private String ldmMinorVersion;

	@Value("${ea.ldm.description:#{null}}")
	private String ldmDescription;

	@Value("${generation.input:#{null}}")
	private String generationInput;

	@Value("${generation.output:#{null}}")
	private String generationOutput;

	@Value("${generation.language.backend:#{null}}")
	private String generationBackendLanguage;

	@Value("${generation.language.frontend:#{null}}")
	private String generationFrontendLanguage;

	@Value("${generation.command.local:#{null}}")
	private String generationCommand;

	@Value("${generation.allowed:#{null}}")
	private boolean generationAllowed;

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
		ldmPackage = normalizeString(ldmPackage);
		ldmIgnoredPackage = normalizeString(ldmIgnoredPackage);
		ldmMainVersion = normalizeString(ldmMainVersion);
		ldmMinorVersion = normalizeString(ldmMinorVersion);
		ldmDescription = normalizeString(ldmDescription);

		printProperty("interfaceName:           ", interfaceName);
		printProperty("interfaceMainVersion:    ", interfaceMainVersion);
		printProperty("ldmPackage:        ", ldmPackage);
		printProperty("ldmIgnoredPackage: ", ldmIgnoredPackage);
		printProperty("ldmMainVersion:          ", ldmMainVersion);
		printProperty("ldmMinorVersion:         ", ldmMinorVersion);
		printProperty("ldmDescription:          ", ldmDescription);
	}

	@Override
	public void run(String... args) {
		Validations.printRegistry();
		if (StringUtils.hasText(interfaceName) && StringUtils.hasText(interfaceMainVersion)) {
			generator.generate(new OpenApiConfiguration(interfaceName, interfaceMainVersion));
		} else if (StringUtils.hasText(ldmPackage)) {
			Validations.muteValidations();
			generator.generate(new LDMConfiguration()
					.basePackage(ldmPackage)
					.ignoredPackage(ldmIgnoredPackage)
					.mainVersion(ldmMainVersion)
					.minorVersion(ldmMinorVersion)
					.description(ldmDescription)
			);
		} else {
			throw new UnsupportedOperationException("Unsupported input configuration!");
		}

		if (generationAllowed) {
			log.info("OpenAPI generation started!");
			generateOpenApi();
		}

		log.info("Done!");
	}

	private void printProperty(String name, String value) {
		log.info("{}{}", name, StringUtils.hasText(value) ? value : "value is empty");
	}

	private String normalizeString(String value) {
		return StringUtils.hasText(value) && !"null".equals(value) ? value : null;
	}

	private void generateOpenApi() {
		try {
			generateStub("frontend");
			generateStub("backend");
			log.info("OpenAPI generation finished!");
		} catch (Exception e) {
			log.warn("OpenAPI generation failed!", e);
		}
	}

	private void generateStub(String outputDir) throws InterruptedException, IOException {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String command = String.format(generationCommand,
									generationInput,
									outputDir.equals("frontend") ? generationFrontendLanguage : generationBackendLanguage,
									generationOutput + outputDir);
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			processBuilder.command("cmd.exe", "/c", command);
		} else {
			processBuilder.command("sh", "-c", command);
		}

		Process process = processBuilder.start();

		int exitCode = process.waitFor();

		if (exitCode != 0) {
			throw new RuntimeException();
		}

		log.info("{} stub generated.", outputDir);
	}
}
