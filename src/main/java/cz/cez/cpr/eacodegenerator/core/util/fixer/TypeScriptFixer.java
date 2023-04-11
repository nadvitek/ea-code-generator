package cz.cez.cpr.eacodegenerator.core.util.fixer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeScriptFixer extends AbstractFixer {

	public static final String END_OF_PROJECT_IMPORTS = "} from './';";
	public static final String END_OF_MODELS_IMPORTS = "} from '../models';";
	public static final String COMMON_DEPENDENCY = "@cz.cez.cpr.restapi/common";

	protected TypeScriptFixer(Path baseFolder, String extension) {
		super(baseFolder, extension);
	}

	public static void main(String[] args) {
		new TypeScriptFixer(Paths.get("output_typescript"), "ts").fixTypeScript();
	}

	private void fixTypeScript() {
		updateIndexTs(removeSharedFiles());
		line("PROCESSING FILES ONE BY ONE");
		fileMap.values().forEach(this::processOne);
	}

	private void updateIndexTs(List<File> files) {
		File indexTsFile = fileMap.get(INDEX_TS);
		line("Removing lines from: " + indexTsFile);
		if (files.isEmpty()) {
			System.out.println("Do nothing");
			return;
		}
		AtomicReference<String> content = new AtomicReference<>(readFile(indexTsFile));
		files.stream().map(f -> String.format("export * from './%s';", removeExtension(f)))
				.forEach(s -> {
					System.out.println("REMOVE LINE: " + s);
					content.set(content.get().replace(s, ""));
				});

		write(indexTsFile, content.get());

		File[] dtoArray = indexTsFile.getParentFile().listFiles(f -> true);
		if (dtoArray == null || dtoArray.length == 1) {
			System.out.println(String.format("Folder %s conains only %s file", indexTsFile.getParentFile(), indexTsFile));
			removeIndexTs(indexTsFile);
		}
	}

	private void removeIndexTs(File indexTsFile) {

		remove(indexTsFile);
		File parentIndexTs = new File(indexTsFile.getParentFile().getParentFile(), INDEX_TS);

		line("Remove lines from: " + parentIndexTs);
		String importModule = "export * from './models';";
		String content = readFile(parentIndexTs);
		System.out.println("REMOVE LINE: " + importModule);
		content = content.replace(importModule, "");
		write(parentIndexTs, content);
	}

	private void processOne(File file) {
		System.out.println("Processing: " + file.getName());

		String content = readFile(file);
		content = fixRuntimeImport(content);
		content = fixModelImport(content, END_OF_PROJECT_IMPORTS);
		content = fixModelImport(content, END_OF_MODELS_IMPORTS);
		content = fixEmptyModelImport(content);
		write(file, content);
	}

	private String fixModelImport(String content, String endTag) {
		int end = content.indexOf(endTag);
		if (end < 0) {
			return content;
		}

		int start = end;
		while (--start > 0 && content.charAt(start - 1) != '{') ;

		List<String> regular = Stream.of(content.substring(start, end).split(",")).map(String::trim).collect(Collectors.toList());
		List<String> common = getCommonImports(regular);

		if (common.isEmpty()) {
			System.out.println("Shared classes weren't detected");
			return content;
		}

		String regularString = regular.stream().filter(name -> !common.contains(name))
				.collect(Collectors.joining(", ", " ", " "));

		String commonString = regular.stream().filter(common::contains)
				.collect(Collectors.joining(", ", " ", " "));

		String commonSection = String.format("\nimport {%s} from '%s';\n", commonString, COMMON_DEPENDENCY);

		System.out.println("Imports will be split in 2 groups!");
		System.out.println("Shared classes: " + commonString);
		System.out.println("Project classes:" + regularString);

		content = content.substring(0, end + endTag.length())
				+ commonSection
				+ content.substring(end + endTag.length() + 1);

		content = content.substring(0, start) + regularString + content.substring(end);

		return content;
	}

	private List<String> getCommonImports(List<String> imports) {
		return imports.stream().filter(name -> {
			int index = name.lastIndexOf("Dto");
			return index > 0 && !fileMap.containsKey(name.substring(0, index + 3) + ".ts");
		}).collect(Collectors.toList());
	}

	private String fixRuntimeImport(String content) {
		content = content.replace("import * as runtime from '../runtime'",
				String.format("import { runtime } from '%s'", COMMON_DEPENDENCY));
		content = content.replace("} from '../runtime'",
				String.format("} from '%s'", COMMON_DEPENDENCY));
		return content;
	}

	private String fixEmptyModelImport(String content) {
		return content.replace("import {  } from '../models';", "");
	}
}
