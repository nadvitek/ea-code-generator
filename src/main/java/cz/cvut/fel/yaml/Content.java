package cz.cvut.fel.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a content of response
 * which is what is a part of response
 */
public class Content extends Component {

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
