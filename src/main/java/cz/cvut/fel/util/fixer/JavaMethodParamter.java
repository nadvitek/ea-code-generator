package cz.cvut.fel.util.fixer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaMethodParamter {

	private static final String BR = "\n";
	private static final Pattern OVERRIDE_TYPE_PATTERN = Pattern.compile("\\$.+\\$");

	private final String annotations;
	private final String type;
	private final String name;
	private final String domainPackage;
	private String overrideType;

	public JavaMethodParamter(String annotations, String type, String name, String dtoPackage) {
		this.annotations = annotations;
		this.type = type;
		this.name = name;
		this.domainPackage = dtoPackage;
		initOverrideType();
	}

	private void initOverrideType() {
		if(annotations == null) {
			return;
		}
		Matcher matcher = OVERRIDE_TYPE_PATTERN.matcher(annotations);
		if (matcher.find()) {
			int start = matcher.start() + 1;
			int end = matcher.end() - 1;
			this.overrideType = domainPackage + "." + toUpperCase(annotations.substring(start, end)) + "Dto";
		}
	}

	private String toUpperCase(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	@Override
	public String toString() {
		return "  - JavaMethodParamter" + BR
				+ "    - annotations: " + annotations + BR
				+ "    - type:        " + type + (overrideType == null ? "" : " (" + overrideType + ")") + BR
				+ "    - name:        " + name;
	}

	public String toStringWithAnnotations() {
		return annotations + " " + getType() + " " + name;
	}

	public String toStringWithoutAnnotations() {
		return getType() + " " + name;
	}

	private String getType() {
		return overrideType == null ? type : overrideType;
	}
}
