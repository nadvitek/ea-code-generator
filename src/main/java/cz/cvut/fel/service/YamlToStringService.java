package cz.cvut.fel.service;

import cz.cvut.fel.util.YamlCreator;
import cz.cvut.fel.yaml.ComplexSchema;
import cz.cvut.fel.yaml.Component;
import cz.cvut.fel.yaml.Content;
import cz.cvut.fel.yaml.Method;
import cz.cvut.fel.yaml.Model;
import cz.cvut.fel.yaml.Parameter;
import cz.cvut.fel.yaml.PrimitiveSchema;
import cz.cvut.fel.yaml.Property;
import cz.cvut.fel.yaml.Request;
import cz.cvut.fel.yaml.Response;
import cz.cvut.fel.yaml.Schema;
import cz.cvut.fel.yaml.SrcCard;
import cz.cvut.fel.yaml.Yaml;
import cz.cvut.fel.yaml.enums.SchemaFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * This class represents a service
 * that takes Model and makes a String from it
 * that represents Swagger
 */
@org.springframework.stereotype.Component
public class YamlToStringService {

	public static final YamlCreator.YamlConsumer EMPTY_CONSUMER = y -> {
	};

	@Value("${ea.debug.id:false}")
	private boolean debugId;

	public String toString(Model model) {
		return y()
				.ln(0, id(model), cnd(debugId))
				.ln(0, "openapi: ", text(model.getOpenApiVersion()))
				.ln(0, "info:")
				.ln(1, "description: ", text(model.getDescription()))
				.ln(1, "title: ", text(model.getTitle()))
				.ln(1, "version: ", text(model.getVersion()))
				.ln(0, "tags:")
				.ln(1, tag(model))
				.ln(0, paths(model.getMethods()))
				.ln(0, components(model.getSchemas()))
				.toString();
	}

	private YamlCreator.YamlConsumer tag(Model model) {
		return y -> model.getTags().forEach(t -> y
				.ln(0, "-")
				.ln(1, "name: ", t.getName())
				.ln(1, "description: ", text(t.getDescription())));
	}

	private YamlCreator.YamlConsumer components(List<Schema> schemas) {
		return y -> y
				.ln(0, "components:")
				.ln(1, "schemas:")
				.ln(2, schemas(schemas));
	}

	private YamlCreator.YamlConsumer paths(Map<String, List<Method>> map) {
		return y -> {
			y.ln(0, "paths:");
			map.forEach((k, v) -> y
					.ln(1, k, ":")
					.ln(2, method(v)));
		};
	}

	private YamlCreator.YamlConsumer method(List<Method> list) {
		return y -> list.forEach(m -> y
				.ln(0, id(m), cnd(debugId))
				.ln(0, m.getType(), ":")
				.ln(1, "tags:")
				.ln(2, "- ", m.getModel().matchingTag(m.getPath()))
				.ln(1, "summary: ", v(m.getSummary()))
				.ln(1, "description: ", text(m.getDescription()))
				.ln(1, "operationId: ", v(m.getOperationId()))
				.ln(1, request(m.getRequest()))
				.ln(1, responses(m.getResponses()))
		);
	}

	private YamlCreator.YamlConsumer request(Optional<Request> request) {
		return y -> request.ifPresent(r -> y
				.ln(0, id(r), cnd(debugId))
				.ln(0, parameters(r.getParameters()))
				.ln(0, "requestBody:", cnd(r.getContents().size() > 0))
				.ln(1, requiredContent(r.getContents()), cnd(r.getContents().size() > 0))
				.ln(1, contents(r.getContents(), r))
		);
	}

	private YamlCreator.YamlConsumer parameters(List<Parameter> parameters) {
		return y -> {
			y.ln(0, "parameters: ", cnd(parameters.size() > 0));
			parameters.forEach(p -> y
					.ln(1, parameter(p)));
		};
	}

	private YamlCreator.YamlConsumer parameter(Parameter parameter) {
		return y -> y
				.ln(0, "-")
				.ln(0, id(parameter), cnd(debugId))
				.ln(1, "in: ", parameter.getIn())
				.ln(1, "name: ", parameter.getName())
				.ln(1, "description: ", text(parameter.getDescription()))
				.ln(1, schema(parameter.getSchema(), parameter.getSrcCard(), SchemaFormat.SCHEMA_FULL))
				.ln(1, "example: ", text(parameter.getExample()))
				.ln(1, "required: ", parameter.getSrcCard().isRequired());
	}

	private YamlCreator.YamlConsumer schemas(List<Schema> list) {
		return y -> list.stream().sorted().forEach(s -> y
				.ln(0, schema(s, s.getSrcCard(), SchemaFormat.NAME_FULL)));
	}

	private YamlCreator.YamlConsumer properties(ComplexSchema schema) {
		return schema.getAllOf() == null ? properties(schema.getProperties()) :
				y -> y
						.ln(0, "allOf:")
						.ln(1, "- $ref: ", pathRef(schema.getAllOf()))
						.ln(1, "- type: object")
						.ln(2, properties(schema.getProperties()));
	}

	private YamlCreator.YamlConsumer properties(List<Property> list) {
		return list.isEmpty() ? EMPTY_CONSUMER : y -> {
			y.ln(0, "properties:");
			list.forEach(p -> y
					.ln(1, schema(p.getSchema(), p.getSrcCard(), SchemaFormat.NAME_REF)));
		};
	}

	private YamlCreator.YamlConsumer required(List<Property> list) {
		return list.isEmpty() ? EMPTY_CONSUMER : y -> {
			y.ln(0, "required: ");
			list.forEach(p -> y.ln(1, "- ", p.getSrcCard().getName()));
		};
	}

	private YamlCreator.YamlConsumer schema(Schema schema, SrcCard srcCard, SchemaFormat format) {
		if (schema instanceof PrimitiveSchema) {
			return primitiveSchema((PrimitiveSchema) schema, srcCard, format);
		} else if (schema instanceof ComplexSchema) {
			return complexSchema((ComplexSchema) schema, srcCard, format);
		} else {
			throw new IllegalArgumentException("Unsupported schema type: " + schema.getClass());
		}
	}

	private YamlCreator.YamlConsumer primitiveSchema(PrimitiveSchema schema, SrcCard srcCard, SchemaFormat fm) {
		return y -> y
				.ln(0, id(schema), cnd(debugId))
				.ln(0, "schema: ", cnd(fm.isFull()))
				.ln(0, srcCard.getName(), ":", cnd(fm.isRef()))
				.ln(1, "description: ", text(schema.getDescription()))
				.ln(1, array(schema), cnd(srcCard.isArray()))
				.ln(1, item(schema), cnd(!srcCard.isArray()));
	}

	private YamlCreator.YamlConsumer array(PrimitiveSchema schema) {
		return y -> y
				.ln(0, "type: ", "array")
				.ln(0, "items: ")
				.ln(1, item(schema));
	}

	private YamlCreator.YamlConsumer item(PrimitiveSchema schema) {
		return y -> y
				.ln(0, "type: ", schema.getType())
				.ln(0, "format: ", v(schema.getFormat()))
				.ln(0, "example: ", text(schema.getExample()));
	}

	private YamlCreator.YamlConsumer complexSchema(ComplexSchema schema, SrcCard srcCard, SchemaFormat fm) {
		return y -> y
				.ln(0, comment(schema.getPath()), cnd(fm == SchemaFormat.NAME_FULL))
				.ln(0, id(schema), cnd(debugId))
				.ln(0, "schema:", cnd(fm.hasSchema()))
				.ln(0, schema.getName(), ":", cnd(fm == SchemaFormat.NAME_FULL))
				.ln(0, srcCard.getName(), ":", cnd(fm == SchemaFormat.NAME_REF))
				.ln(1, ref(schema, srcCard), cnd(fm.isRef()))
				.ln(1, "description: ", text(schema.getDescription()), cnd(fm.isFull()))
				.ln(1, "type: ", "object", cnd(fm.isFull()))
				.ln(1, discriminator(schema), cnd(fm.isFull() && !schema.getMappingSet().isEmpty()))
				.ln(1, properties(schema), cnd(fm.isFull()))
				.ln(1, required(schema.getRequired()), cnd(fm.isFull()));
	}

	private String comment(String text) {
		StringBuilder sb = new StringBuilder();
		Scanner scanner = new Scanner(String.valueOf(text));
		while (scanner.hasNextLine()) {
			sb.append(sb.length() > 0 ? YamlCreator.br() : "")
					.append("# ").append(scanner.nextLine());
		}
		return sb.toString();
	}

	private YamlCreator.YamlConsumer discriminator(ComplexSchema schema) {
		return y -> {
			y
					.ln(0, "discriminator: ")
					.ln(1, "propertyName: ", schema.getPropertyName())
					.ln(1, "mapping: ");
			schema.getMappingSet().forEach(child -> y.ln(2, child.getPropertyName(), ": ", pathRef(child),
					" # id: ", child.getEaId()));
		};
	}

	private YamlCreator.YamlConsumer ref(ComplexSchema schema, SrcCard srcCard) {
		return y -> y
				.ln(0, "type: array", cnd(srcCard.isArray()))
				.ln(0, "items:", cnd(srcCard.isArray()))
				.ln(0, ref(schema), cnd(!srcCard.isArray()))
				.ln(1, ref(schema), cnd(srcCard.isArray()));
	}

	private YamlCreator.YamlConsumer ref(ComplexSchema schema) {
		return y -> y.ln(0, "$ref: ", pathRef(schema));
	}

	private String pathRef(ComplexSchema schema) {
		return "'#/components/schemas/" + schema.getName() + "'";
	}

	private YamlCreator.YamlConsumer responses(List<Response> responses) {
		return y -> {
			y.ln(0, "responses: ", cnd(responses.size() > 0));
			responses.stream().sorted().forEach(r -> y.ln(1, response(r)));
		};
	}

	private YamlCreator.YamlConsumer response(Response response) {
		return y -> y
				.ln(0, id(response), cnd(debugId))
				.ln(0, response.getCode(), ":")
				.ln(1, "description: ", text(response.getDescription()))
				.ln(1, contents(response.getContents(), response));
	}

	private YamlCreator.YamlConsumer contents(List<Content> contents, Component parent) {
		return y -> {
			if (contents.isEmpty() || contents.get(0).getSchema() == null) {
				return;
			}
			y.ln(0, id(parent), cnd(debugId))
					.ln(0, "content: ")
					.ln(1, getNotEmptyType(parent), ":");
//					.ln(2, String.format("# size: %s, request: %s, response: %s, content[0].isPrimitive: %s",
//							contents.size(), parent.isRequest(), parent.isResponse(), contents.get(0).getSchema().isPrimitiveSchema()));
			if (contents.size() > 1) {
				y.ln(2, primitiveObjects(contents));
			} else if (parent.isRequest() && contents.get(0).getSchema().isPrimitiveSchema()) {
				y.ln(2, primitiveObjects(contents));
			} else if (parent.isResponse() && contents.get(0).getSchema().isPrimitiveSchema()) {
				y.ln(2, schema(contents.get(0).getSchema(), contents.get(0).getSrcCard(), SchemaFormat.SCHEMA_FULL));
			} else {
				y.ln(2, schema(contents.get(0).getSchema(), contents.get(0).getSrcCard(), SchemaFormat.SCHEMA_REF));
			}
		};
	}

	private YamlCreator.YamlConsumer primitiveObjects(List<Content> contents) {
		return y -> y
				.ln(0, "schema:")
				.ln(1, "type: object")
				.ln(1, properties(contents.stream().map(c -> {
							if (c.getSchema().isPrimitiveSchema()) {
								return new Property(
										c,
										c.getSchema().getSrcCard(),
										c.getSchema().getEaId(),
										c.getSchema().getEaPath(),
										c.getSchema()
								);
							} else {
								PrimitiveSchema schema = c.getSchema().castToComplexSchema().toPrimitiveSchema();
								return new Property(c, schema.getSrcCard(), schema.getEaId(), schema.getEaPath(), schema);
							}
						}
				).collect(Collectors.toList())));
	}

	private String getNotEmptyType(Component component) {
		String type = getType(component);
		return StringUtils.hasText(type) ? type : "application/json";
	}

	private String getType(Component component) {
		if (component.isRequest()) {
			return component.castToRequest().getContentType();
		} else if (component.isResponse()) {
			return component.castToResponse().getContentType();
		} else {
			return null;
		}
	}

	private YamlCreator.YamlConsumer requiredContent(List<Content> contents) {
		return y -> y
				.ln(0, "required: ", contents.size() == 1 && contents.get(0).getSrcCard().isRequired());
	}

	private YamlCreator y() {
		return new YamlCreator();
	}

	private YamlCreator.Condition cnd(boolean b) {
		return new YamlCreator.Condition(b);
	}

	private YamlCreator.Value v(Object o) {
		return new YamlCreator.Value(o);
	}

	private YamlCreator.Text text(Object o) {
		return new YamlCreator.Text(o);
	}

	private String id(Component c) {
		return new StringBuilder("# ")
				.append(c.getClass().getSimpleName())
				.append(".id: ")
				.append(c.getEaId())
				.append(", srcCard:")
				.append(c.getSrcCard())
				.append(", ")
				.append(c.getEaPath())
				.toString();
	}
}
