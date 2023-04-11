package cz.cez.cpr.eacodegenerator.core.yaml;

public class Property extends Component {

	private Schema schema;

	public Property(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	public Property(Component parent, SrcCard srcCard, long eaId, String eaPath, Schema schema) {
		super(parent, srcCard, eaId, eaPath);
		schema(schema);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	@Override
	public void validate() {
		// TODO
	}

	public Schema schema(Schema schema) {
		this.schema = schema;
		return schema;
	}

	public Schema getSchema() {
		return schema;
	}
}
