package cz.cez.cpr.eacodegenerator.core.util.fixer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class JavaMethodDefinition {

	private static final String BR = "\n";
	public static final String ANNOTATED_PARAMETER_SEPARATOR = ",[ ]*@";
	public static final String NOT_ANNOTATED_PARAMETER_SEPARATOR = ",[ ]*";

	private String modifier;
	private String returnType;
	private String name;
	private final String dtoPackage;
	private List<JavaMethodParamter> parameters = new ArrayList<>();

	private JavaMethodDefinition(String dtoPackage) {
		this.dtoPackage = dtoPackage;
	}

	public static JavaMethodDefinition of(String definition, String dtoPackage) {

		definition = definition.trim();
		int openBracket = definition.indexOf("(");
		int closeBracket = definition.lastIndexOf(")");
		if (!definition.startsWith("default") || openBracket < 0 || closeBracket < openBracket) {
			return null;
		}

		String signature = definition.substring(0, openBracket);
		String parameters = definition.substring(openBracket + 1, closeBracket);

		JavaMethodDefinition method = new JavaMethodDefinition(dtoPackage);
		method.processSignature(signature);
		method.processParameters(parameters);

		return method;
	}

	private void processParameters(String parameters) {
		this.parameters = splitParameters(parameters).stream().map(s -> {
			Scanner scanner = new Scanner(reverse(s));
			String name = reverse(scanner.next()).trim();
			String type = reverse(scanner.next()).trim();
			scanner.useDelimiter("\\A");
			String annotations = scanner.hasNext() ? reverse(scanner.next()).trim() : null;
			return new JavaMethodParamter(annotations, type, name, dtoPackage);
		}).collect(Collectors.toList());
	}

	private String reverse(String s) {
		return new StringBuilder(s).reverse().toString();
	}

	private List<String> splitParameters(String parameters) {
		List<String> list = splitAnnotatedParameters(parameters);
		if (!list.isEmpty() && !list.get(0).startsWith("@")) {
			list = splitNotAnnotatedParameters(parameters);
		}
		return list;
	}

	private List<String> splitAnnotatedParameters(String parameters) {
		List<String> parts = new ArrayList<>();
		String[] split = parameters.split(ANNOTATED_PARAMETER_SEPARATOR);
		for (int i = 0; i < split.length; i++) {
			parts.add(i == 0 ? split[i] : "@" + split[i]);
		}
		return filterNotEmpty(parts);
	}

	private List<String> splitNotAnnotatedParameters(String parameters) {
		return filterNotEmpty(Arrays.asList(parameters.split(NOT_ANNOTATED_PARAMETER_SEPARATOR)));
	}

	private List<String> filterNotEmpty(List<String> list) {
		return list.stream().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
	}

	private void processSignature(String signature) {
		List<String> parts = split(signature);
		this.name = parts.get(parts.size() - 1);
		this.returnType = parts.get(parts.size() - 2);
		this.modifier = parts.stream().limit(parts.size() - 2).collect(Collectors.joining(" "));
	}

	private List<String> split(String signature) {
		Scanner scanner = new Scanner(signature);
		List<String> parts = new ArrayList<>();
		while (scanner.hasNext()) {
			parts.add(scanner.next().trim());
		}
		return parts;
	}

	@Override
	public String toString() {
		return "JavaMethodDefinition:" + BR
				+ "- modifier:   " + modifier + BR
				+ "- returnType: " + returnType + BR
				+ "- name:       " + name + BR
				+ "- parameters: " + BR
				+ parameters.stream().map(JavaMethodParamter::toString).collect(Collectors.joining(BR));
	}

	public boolean isApiMethod() {
		return returnType.startsWith("ResponseEntity");
	}

	public String toStringWithAnnotations() {
		return "    " + modifier + " " + returnType + " " + name + "(" +
				parameters.stream().map(JavaMethodParamter::toStringWithAnnotations).collect(Collectors.joining(", ")) + ") {";
	}

	public String toStringWithoutAnnotations() {
		return "    " + modifier + " " + returnType + " " + name.replaceFirst("_", "") + "(" +
				parameters.stream().map(JavaMethodParamter::toStringWithoutAnnotations).collect(Collectors.joining(", ")) + ") {";
	}
}
