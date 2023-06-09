package cz.cvut.fel.yaml;

/**
 * This class represents Property section
 * used in Swagger files
 */
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

	public Schema schema(Schema schema) {
		this.schema = schema;
		return schema;
	}

	public Schema getSchema() {
		return schema;
	}
}
