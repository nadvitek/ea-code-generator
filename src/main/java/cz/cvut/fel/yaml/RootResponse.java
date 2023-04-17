package cz.cvut.fel.yaml;

import java.util.ArrayList;
import java.util.List;

public class RootResponse extends Component {

	private final List<Response> responses = new ArrayList<>();

	public RootResponse(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	public Response response(Response response) {
		this.responses.add(response);
		return response;
	}

	public List<Response> getResponses() {
		return responses;
	}
}
