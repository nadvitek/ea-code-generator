package cz.cvut.fel.generator;

import cz.cvut.fel.configuration.OpenApiConfiguration;
import cz.cvut.fel.metamodel.TObject;
import cz.cvut.fel.metamodel.TPackage;
import cz.cvut.fel.repository.TPackageRepository;
import cz.cvut.fel.util.QualifiedNameGenerator;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractGenerator {

	protected final TPackageRepository packageRepository;

	private static final Logger log = LoggerFactory.getLogger(AbstractGenerator.class);

	protected AbstractGenerator(TPackageRepository packageRepository) {
		this.packageRepository = packageRepository;
	}

	public abstract void generate(OpenApiConfiguration eaConfiguration);

	protected void cleanFolder(String folder) {
		FileSystemUtils.deleteRecursively(new File(folder));
	}

	protected void write(String text, String fileName, String exportFolder) {
		File file = new File(exportFolder, fileName);
		log.info("Creating file: {}", file.getAbsolutePath());
		createFile(file);
		write(file, text);
	}

	protected TPackage getPackageByPath(String path) {
		Validate.notEmpty(path, "Path cannot be null or empty.");
		String[] packageNames = StringUtils.split(path, '/');
		long parentPackageId = 0L;
		TPackage retVal = null;
		List<TPackage> foundPackages = new ArrayList<>();

		for (String packageName : packageNames) {
			List<TPackage> childPackages = packageRepository.findTPackageByParentId(parentPackageId);

			for (TPackage childPackage : childPackages) {
				if (packageName.equalsIgnoreCase(childPackage.getName())) {
					foundPackages.add(childPackage);
				}
			}

			if (foundPackages.isEmpty()) {
				return null;
			}
			Validate.isTrue(foundPackages.size() == 1,
					"Duplicit package names: " + foundPackages.toString() + ".");

			retVal = foundPackages.get(0);
			parentPackageId = retVal.getId();
			foundPackages.clear();
		}
		return retVal;
	}

	private void createFile(File file) {
		try {
			file.getParentFile().delete();
			file.getParentFile().mkdirs();
			new FileWriter(file, false).close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void write(File file, String text) {
		try {
			text += System.lineSeparator();
			Files.write(Paths.get(file.toURI()), text.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected TObject log(TObject o) {
		log.info("Processing [{}]: '{}'", o.getType(), getEaPath(o));
		return o;
	}

	protected String getEaPath(TObject o) {
		return QualifiedNameGenerator.getFullName(o, new HashSet<>(), null);
	}

	protected String readTemplate(String templateName) {
		try {
			return resourceToString(new ClassPathResource("template/" + templateName, this.getClass().getClassLoader()));
		} catch (Exception e) {
			throw new IllegalArgumentException("Can't read template", e);
		}
	}

	private String resourceToString(Resource resource) throws IOException {
		return new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines()
				.collect(Collectors.joining("\n"));
	}

	protected String decodeUrl(String url) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, StandardCharsets.UTF_8.name());
	}
}
