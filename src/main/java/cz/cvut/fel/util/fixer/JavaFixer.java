package cz.cvut.fel.util.fixer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class JavaFixer extends AbstractFixer {

	public JavaFixer(Path baseFolder, String extension) {
		super(baseFolder, extension);
	}

	public static void main(String[] args) {
		new JavaFixer(Paths.get("", "output_java", "src", "main", "java"), "java").fixJava();
	}

	private void fixJava() {
		removeSharedFiles();
		fixApiMethods(findApiFile());
	}

	private void fixApiMethods(File apiFile) {
		line("FIX API METHODS");
		String text = readFile(apiFile);
		String dtoPackage = getPackage(text).replace(".controller", ".dto");

		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(text);

		JavaMethodDefinition annotatedMethod = null;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			JavaMethodDefinition method = JavaMethodDefinition.of(line, dtoPackage);
			if (method == null || !method.isApiMethod()) {
				sb.append(line).append(BR);
				continue;
			} else {
				System.out.println(method);
			}
			sb.append("//  Replaced with: ").append(this.getClass().getName()).append(BR)
					.append("// ").append(line.replaceFirst("   ", "")).append(BR);
			if (annotatedMethod == null) {
				sb.append(method.toStringWithAnnotations()).append(BR);
				annotatedMethod = method;
			} else {
				sb.append(annotatedMethod.toStringWithoutAnnotations()).append(BR);
				annotatedMethod = null;
			}
		}
		write(apiFile, sb.toString());
	}

	private String getPackage(String text) {
		Scanner scanner = new Scanner(text);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("package cz.cez.cpr")) {
				return line.replace("package", "").replace(";", "").trim();
			}
		}
		throw new RuntimeException("Can't obtain package");
	}

}
