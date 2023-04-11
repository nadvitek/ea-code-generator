package cz.cez.cpr.eacodegenerator.core.yaml;

import cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype;

public class Parameter extends Component {

	private Stereotype in;
	private String name;
	private String description;
	private Schema schema;
	private String example;

	public Parameter(Component parent, SrcCard srcCard, long eaId, String eaPath) {
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

	public Parameter in(Stereotype in) {
		this.in = in;
		return this;
	}

	public Stereotype getIn() {
		return in;
	}

	public Parameter name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public Parameter description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Parameter schema(Schema schema) {
		this.schema = schema;
		return this;
	}

	public Schema getSchema() {
		return schema;
	}

	public Parameter example(String example) {
		this.example = example;
		return this;
	}

	public String getExample() {
		return example;
	}
}
