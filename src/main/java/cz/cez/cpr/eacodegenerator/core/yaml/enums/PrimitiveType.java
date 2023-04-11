package cz.cez.cpr.eacodegenerator.core.yaml.enums;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PrimitiveType {

	ARRAY("array"),
	BOOLEAN("boolean"),
	INTEGER("integer", PrimitiveTypeFormat.INT_64),
	NUMBER("number"),
	OBJECT("object"),
	STRING("string"),
	ENUM("enum"),

	INT("int", INTEGER, PrimitiveTypeFormat.INT_64),
	FLOAT("float", NUMBER),
	DATE("date", STRING, PrimitiveTypeFormat.DATE),
	DATE_TIME("datetime", STRING, PrimitiveTypeFormat.DATE_TIME),
	CHAR("char", STRING),
	BYTE("byte", STRING, PrimitiveTypeFormat.BYTE),
	BINARY("binary", STRING, PrimitiveTypeFormat.BINARY);

	private final String eaValue;
	private final String openApiValue;
	private final PrimitiveTypeFormat openApiFormat;

	PrimitiveType(String eaValue) {
		this(eaValue, (PrimitiveTypeFormat) null);
	}

	PrimitiveType(String eaValue, PrimitiveTypeFormat openApiFormat) {
		this.eaValue = eaValue;
		this.openApiValue = eaValue.toLowerCase();
		this.openApiFormat = openApiFormat;
	}

	PrimitiveType(String eaValue, PrimitiveType openApiValue) {
		this(eaValue, openApiValue, null);
	}

	PrimitiveType(String eaValue, PrimitiveType openApiValue, PrimitiveTypeFormat openApiFormat) {
		this.eaValue = eaValue;
		this.openApiValue = openApiValue.getOpenApiValue();
		this.openApiFormat = openApiFormat;
	}

	public static PrimitiveType of(String value) {
		return Stream.of(values()).filter(v -> v.eaValue.equals(value)).findAny().orElse(null);
	}

	public static PrimitiveType ofIgnoreCase(String value) {
		return value == null ? null : of(value.toLowerCase());
	}

	public static String asString() {
		return Stream.of(values()).map(PrimitiveType::getEaValue).collect(Collectors.toList()).toString();
	}

	public String getOpenApiValue() {
		return openApiValue;
	}

	public PrimitiveTypeFormat getOpenApiFormat() {
		return openApiFormat;
	}

	private String getEaValue() {
		return eaValue;
	}

	@Override
	public String toString() {
		return eaValue;
	}
}
