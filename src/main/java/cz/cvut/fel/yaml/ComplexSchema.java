package cz.cvut.fel.yaml;

import cz.cvut.fel.yaml.enums.PrimitiveType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents a Complex schema
 * used in Swagger file. It's meant to be a class
 * in schema
 */
public class ComplexSchema extends Schema {

	private final List<Property> properties = new ArrayList<>();
	private final Set<ComplexSchema> mappingSet = new HashSet<>();
	private ComplexSchema allOf = null;
	private String path;

	public ComplexSchema(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		model.addSchema(this);
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

	public void mapping(ComplexSchema schema) {
		mappingSet.add(schema);
	}

	public Set<ComplexSchema> getMappingSet() {
		return mappingSet;
	}

	public void allOf(ComplexSchema allOf) {
		this.allOf = allOf;
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
