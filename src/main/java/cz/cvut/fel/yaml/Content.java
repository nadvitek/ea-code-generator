package cz.cvut.fel.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Content extends Component {

	private static final Logger log = LoggerFactory.getLogger(Content.class);

	private Schema schema;

	public Content(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	public Schema schema(Schema schema) {
		if (schema != null) {
			this.schema = schema;
		}
		return schema;
	}

	public Schema getSchema() {
		return schema;
	}
}
