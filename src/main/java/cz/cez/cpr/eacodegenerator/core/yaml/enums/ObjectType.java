package cz.cez.cpr.eacodegenerator.core.yaml.enums;

import java.util.stream.Stream;

public enum ObjectType {

	CLASS("Class"),
	INTERFACE("Interface"),
	PRIMITIVE_TYPE("PrimitiveType"),
	RESPONSE("Response"),
	API_PATH("ApiPath");

	private final String name;

	ObjectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ObjectType of(String value) {
		return Stream.of(values()).filter(v -> v.name.equals(value)).findAny().orElse(null);
	}

	@Override
	public String toString() {
		return name;
	}
}
