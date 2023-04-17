package cz.cvut.fel.yaml.enums;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PrimitiveTypeFormat {

	INT_32("int32"),
	INT_64("int64"),
	FLOAT("float"),
	DOUBLE("double"),
	BYTE("byte"),
	BINARY("binary"),
	DATE("date"),
	DATE_TIME("date-time"),
	PASSWORD("password");

	private final String name;

	PrimitiveTypeFormat(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static PrimitiveTypeFormat of(String value) {
		return Stream.of(values()).filter(v -> v.name.equals(value)).findAny().orElse(null);
	}

	public static PrimitiveTypeFormat ofIgnoreCase(String value) {
		return value == null ? null : of(value.toLowerCase());
	}

	public static String asString() {
		return Stream.of(values()).map(PrimitiveTypeFormat::getName).collect(Collectors.toList()).toString();
	}

	@Override
	public String toString() {
		return name;
	}
}
