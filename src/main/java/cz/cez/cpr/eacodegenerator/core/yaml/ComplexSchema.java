package cz.cez.cpr.eacodegenerator.core.yaml;

import cz.cez.cpr.eacodegenerator.core.yaml.enums.PrimitiveType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ComplexSchema extends Schema {

	private List<Property> properties = new ArrayList<>();
	private Set<ComplexSchema> mappingSet = new HashSet<>();
	private ComplexSchema allOf = null;
	private String path;

	public ComplexSchema(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		model.addSchema(this);
	}

	@Override
	public void validate() {
		// TODO
	}

	public Property property(Property property) {
		this.properties.add(property);
		return property;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public List<Property> getRequired() {
		return properties.stream()
				.filter(p -> p.getSrcCard().isRequired())
				.collect(Collectors.toList());
	}

	public ComplexSchema mapping(ComplexSchema schema) {
		mappingSet.add(schema);
		return this;
	}

	public Set<ComplexSchema> getMappingSet() {
		return mappingSet;
	}

	public ComplexSchema allOf(ComplexSchema allOf) {
		this.allOf = allOf;
		return this;
	}

	public ComplexSchema getAllOf() {
		return allOf;
	}

	public String getPropertyName() {
		String discriminator = String.valueOf(getName());
		return discriminator.substring(0, 1).toLowerCase() + discriminator.substring(1);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public PrimitiveSchema toPrimitiveSchema() {
		return new PrimitiveSchema(this, this.getSrcCard(), this.getEaId(), this.getEaPath())
				.type(PrimitiveType.OBJECT)
				.description((getDescription() == null ? "" : getDescription() + " ") + "($" + getSrcCard().getName() + "$)")
				.castToPrimitiveSchema()
				;
	}
}
