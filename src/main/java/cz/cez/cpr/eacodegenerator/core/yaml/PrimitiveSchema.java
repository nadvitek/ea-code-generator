package cz.cez.cpr.eacodegenerator.core.yaml;

import cz.cez.cpr.eacodegenerator.core.yaml.enums.PrimitiveType;
import cz.cez.cpr.eacodegenerator.core.yaml.enums.PrimitiveTypeFormat;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveSchema extends Schema {

	private String example;
	private PrimitiveType type;
	private PrimitiveTypeFormat format;
	private List<String> enumValues = new ArrayList<>();

	public PrimitiveSchema(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	@Override
	public void validate() {
		// TODO
	}

	public PrimitiveSchema example(String example) {
		this.example = example;
		return this;
	}

	public String getExample() {
		return example;
	}

	public PrimitiveSchema type(PrimitiveType type) {
		this.type = type == PrimitiveType.ENUM ? PrimitiveType.STRING : type;
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

	public List<String> getEnumValues() {
		return enumValues;
	}

	public void addEnumValue(String value) {
		this.enumValues.add(value);
	}
}
