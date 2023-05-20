package cz.cvut.fel.yaml;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents Request from EA
 * and Swagger file
 */
public class Request extends Component {

	private final List<Parameter> parameters = new ArrayList<>();
	private final List<Content> contents = new ArrayList<>();
	private String contentType;

	public Request(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	public Content content(Content content) {
		this.contents.add(content);
		return content;
	}

	public Request contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public void parameter(Parameter parameter) {
		this.parameters.add(parameter);
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<Content> getContents() {
		return contents;
	}
}
