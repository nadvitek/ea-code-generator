package cz.cvut.fel.util.validations;

import cz.cvut.fel.metamodel.TConnector;
import cz.cvut.fel.metamodel.TObject;
import cz.cvut.fel.yaml.Schema;
import cz.cvut.fel.yaml.enums.ObjectType;
import cz.cvut.fel.yaml.enums.PrimitiveType;
import cz.cvut.fel.yaml.enums.PrimitiveTypeFormat;
import cz.cvut.fel.yaml.enums.Stereotype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cz.cvut.fel.util.validations.Validation.create;

public class Validations {

	private static final Logger log = LoggerFactory.getLogger(Validations.class);
	private static final List<Validation<?, ?>> registry = new ArrayList<>();

	public static final String TYPE = "type";
	private static final String DESCRIPTION = "ApiDescription";
	private static final String EXAMPLE = "example";
	private static final String FORMAT = "format";
	private static final String VERSION = "ApiBuild";
	private static final String OPEN_API_VERSION = "OpenApiVersion";
	private static final String API_CONTENT_TYPE = "ApiContentType";

	public static final Validation<TObject, String> V_PRIMITIVE_TYPE = Validation.notNullAttribute(TYPE).hard();
	public static final Validation<String, PrimitiveType> V_PRIMITIVE_TYPE_VALID = create(
			PrimitiveType::ofIgnoreCase, Objects::nonNull, (in, out)
					-> String.format("Unsupported %s: %s, supported (case ignore) types: %s", TYPE, in, PrimitiveType.asString()))
			.hard();

	public static final Validation<TObject, String> V_PRIMITIVE_TYPE_FORMAT = Validation.notNullAttribute(FORMAT).minor();
	public static final Validation<String, PrimitiveTypeFormat> V_PRIMITIVE_TYPE_FORMAT_VALID = Validation.create(
			PrimitiveTypeFormat::ofIgnoreCase, (in, out) -> in == null || out != null, (in, out)
					-> String.format("Unsupported %s: %s, supported (case ignore) types: %s", FORMAT, in, PrimitiveTypeFormat.asString()))
			.hard();
	public static final Validation<TObject, String> V_OPEN_API_VERSION = Validation.notNullProperty(OPEN_API_VERSION).soft();
	public static final Validation<TObject, String> V_VERSION = Validation.notNullProperty(VERSION).soft();
	public static final Validation<TObject, String> V_API_CONTENT_TYPE = Validation.notNullProperty(API_CONTENT_TYPE).soft();
	public static final Validation<TObject, String> V_DESCRIPTION = Validation.notNullProperty(DESCRIPTION).minor();
	public static final Validation<TObject, String> V_EXAMPLE = Validation.notNullPropertyIgnoreCase(EXAMPLE).minor();

	public static final Validation<ObjectType, Schema> V_INVALID_SCHEMA = Validation.create((ObjectType o) -> null, (Schema o) -> true,
			(in, out) -> String.format("Unsupported combination for schema: {type=%s, typeObject=null}", in)).hard();

	public static final Validation<TObject, String> V_OBJECT_NAME = Validation.notNullObjectName().hard();
	public static final Validation<TConnector, String> V_CONNECTOR_NAME = Validation.notNullConnectorName().hard();

	public static final Validation<TConnector, Stereotype> V_PARAMETER_STEREOTYPE = Validation.create(
			(TConnector c) -> Stereotype.of(c.getStereotype()), s -> s == Stereotype.API_PATH || s == Stereotype.API_QUERY,
			(in, out) -> String.format("Unsupported parameter stereotype: %s", in == null ? null : in.getStereotype())).hard();

	public static void register(Validation<?, ?> validation) {
		registry.add(validation);
	}

	public static void muteValidations() {
		registry.forEach(Validation::mute);
	}

	public static void printRegistry() {
		log.info("=== List of validations used in EAGenerator ===");
		registry.stream().collect(Collectors.groupingBy(Validation::getClass))
				.forEach((key, value) -> {
					AtomicInteger id = new AtomicInteger(1);
					log.info("> {}s ({})", key.getSimpleName(), value.get(0).getTypeDescription());
					value.forEach(v -> log.info("{}: {}", id.getAndIncrement(), v));
				});
		log.info("===============================================");
	}
}
