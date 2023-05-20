package cz.cvut.fel.yaml.enums;

import java.util.stream.Stream;

/**
 * This enum represents different Object
 * types in EA
 */
public enum ObjectType {

	CLASS("Class"),
	INTERFACE("Interface"),
	PRIMITIVE_TYPE("PrimitiveType");

	private final String name;

	ObjectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * this method gets the type of given value
	 * @param value - value that we are getting the type of
	 * @return - value type
	 */
	public static ObjectType of(String value) {
		return Stream.of(values()).filter(v -> v.name.equals(value)).findAny().orElse(null);
	}

	@Override
	public String toString() {
		return name;
	}
}
