package cz.cvut.fel.yaml;

import cz.cvut.fel.yaml.enums.PrimitiveType;
import cz.cvut.fel.yaml.enums.PrimitiveTypeFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents Primitive Schema
 * used in swagger - that means attributes from class
 * from EA
 */
public class PrimitiveSchema extends Schema {

	private String example;
	private PrimitiveType type;
	private PrimitiveTypeFormat format;

	public PrimitiveSchema(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	public PrimitiveSchema example(String example) {
		this.example = example;
		return this;
	}

	public String getExample() {
		return example;
	}

	public PrimitiveSchema type(PrimitiveType type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type == null ? null : type.getOpenApiValue();
	}

	public PrimitiveSchema format(PrimitiveTypeFormat format) {
		this.format = format;
		return this;
	}

	public PrimitiveTypeFormat getFormat() {
		return type != null && type.getOpenApiFormat() != null ? type.getOpenApiFormat() : format;
	}
}
