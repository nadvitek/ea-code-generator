package cz.cez.cpr.eacodegenerator.core.yaml.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum Stereotype {

	METHOD_GET("GetMethod", "get"),
	METHOD_POST("PostMethod", "post"),
	METHOD_PUT("PutMethod", "put"),
	METHOD_DELETE("DeleteMethod", "delete"),
	METHOD_PATCH("PatchMethod", "patch"),

	REQUEST("Request", null),
	RESPONSE("Response", null),
	API_PATH("ParameterInPath", "path"),
	API_QUERY("ParameterInQuery", "query"),
	API_BODY("ParameterInBody", null),

	CODEBOOK("codebook", null);

	private static final List<Stereotype> METHODS = Arrays.asList(
			METHOD_GET,
			METHOD_POST,
			METHOD_PUT,
			METHOD_DELETE,
			METHOD_PATCH
	);

	private final String name;
	private final String yaml;

	Stereotype(String name, String yaml) {
		this.name = name;
		this.yaml = yaml;
	}

	public String getName() {
		return name;
	}

	public static Stereotype of(String value) {
		return Stream.of(values()).filter(v -> v.name.equals(value)).findAny().orElse(null);
	}

	public static boolean isMethodStereotype(String value) {
		return METHODS.contains(of(value));
	}

	public static boolean isMethodStereotype(Stereotype value) {
		return METHODS.contains(value);
	}

	@Override
	public String toString() {
		return yaml;
	}
}