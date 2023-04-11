package cz.cez.cpr.eacodegenerator.core.util.fixer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractFixer {

	public static final String BR = "\n";

	public static final String LINE_SEPARATOR = "------------------------------------------------------------------------";
	protected final Map<String, File> fileMap;
	private final String extension;

	protected static final String INDEX_TS = "index.ts";
	protected static final String DTO_POSTFIX = "Dto";
	protected static final List<String> POSTFIX_LIST = Collections.unmodifiableList(Arrays.asList(
			DTO_POSTFIX,
			"AllOfDto",
			"OneOfDto"
	));

	protected AbstractFixer(Path baseFolder, String extension) {
		this.extension = "." + extension;
		line(String.format("Searching files with extension: *%s", extension));
		this.fileMap = getFileMap(baseFolder);
		System.out.println(String.format("Found %s files:", fileMap.size()));
		fileMap.keySet().forEach(f -> System.out.println("> " + f));
	}

	protected void write(File file, String text) {
		System.out.println("Writing: " + file);
		try {
			Files.write(Paths.get(file.toURI()), text.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected static String readFile(File file) {
		System.out.println("Reading: " + file);
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(file)), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return contentBuilder.toString();
	}

	protected Map<String, File> getFileMap(Path baseFolder) {
		System.out.println("Root directory: " + baseFolder.toAbsolutePath());
		try {
			return Files.find(baseFolder, Integer.MAX_VALUE,
					(p, a) -> p.toFile().getName().endsWith(extension))
					.map(Path::toFile)
					.filter(f -> !f.getName().equals(INDEX_TS) || "models".equals(f.getParentFile().getName()))
					.collect(Collectors.toMap(File::getName, Function.identity()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void line(String message) {
		int left = (LINE_SEPARATOR.length() - message.length() - 2) / 2;
		int right = LINE_SEPARATOR.length() - left - 2 - message.length();
		System.out.println(LINE_SEPARATOR.substring(0, left) + " " + message + " " + LINE_SEPARATOR.substring(0, right));
	}

	protected List<File> removeSharedFiles() {
		line("REMOVING SHARED FILES");
		Set<String> rootDtoSet = getRootDtoSet();
		List<File> potentiallySharedFiles = potentiallySharedFiles();
		return removeSharedFiles(rootDtoSet, potentiallySharedFiles);
	}

	protected List<File> removeSharedFiles(Set<String> rootDtoSet, List<File> potentiallySharedFiles) {
		line("Removing shared files");
		List<File> toRemove = potentiallySharedFiles.stream()
				.filter(f -> !rootDtoSet.contains(getRootClass(f)))
				.collect(Collectors.toList());
		if (toRemove.isEmpty()) {
			System.out.println("Nothing found");
		} else {
			toRemove.forEach(this::remove);
		}
		return toRemove;
	}

	protected void remove(File file) {
		System.out.println("REMOVE FILE: " + file);
		if (!file.delete()) {
			throw new RuntimeException("Can't delete file: " + file);
		}
		fileMap.remove(file.getName());
	}

	protected List<File> potentiallySharedFiles() {
		line("Searching potentially shared files");
		List<File> list = fileMap.values().stream()
				.filter(f -> {
					String postfix = getDtoPostfix(f);
					return postfix != null && !DTO_POSTFIX.equals(postfix);
				})
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			System.out.println("Nothing found");
		} else {
			list.forEach(s -> System.out.println("> " + s));
		}
		return list;
	}

	protected Set<String> getRootDtoSet() {
		line("Creating set of root dto");
		Set<String> set = fileMap.values().stream()
				.filter(f -> DTO_POSTFIX.equals(getDtoPostfix(f)))
				.map(this::getRootClass)
				.collect(Collectors.toSet());
		if (set.isEmpty()) {
			System.out.println("Nothing found");
		} else {
			set.forEach(s -> System.out.println("> " + s));
		}
		return set;
	}

	protected String getRootClass(File file) {
		String postfix = getDtoPostfix(file);
		if (postfix == null) {
			return null;
		}
		return file.getName().substring(0, file.getName().length() - postfix.length() - extension.length());
	}

	protected String getDtoPostfix(File file) {
		String fileName = file.getName();
		int index = POSTFIX_LIST.stream()
				.filter(postfix -> fileName.endsWith(postfix + extension))
				.mapToInt(fileName::lastIndexOf)
				.filter(v -> v > 0)
				.min()
				.orElse(-1);
		if (index < 0) {
			return null;
		}
		return fileName.substring(index, fileName.length() - extension.length());
	}

	protected String removeExtension(File file) {
		return file.getName().substring(0, file.getName().length() - extension.length());
	}

	protected File findApiFile() {
		return fileMap.values().stream().filter(f -> f.getName().endsWith("Api.java")).findAny().get();
	}
}
