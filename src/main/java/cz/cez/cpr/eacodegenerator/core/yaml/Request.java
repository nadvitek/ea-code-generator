package cz.cez.cpr.eacodegenerator.core.yaml;

import java.util.ArrayList;
import java.util.List;

public class Request extends Component {

	private List<Parameter> parameters = new ArrayList<>();
	private List<Content> contents = new ArrayList<>();
	private String contentType;

	public Request(Component parent, SrcCard srcCard, long eaId, String eaPath) {
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

	public Request parameter(Parameter parameter) {
		this.parameters.add(parameter);
		return this;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<Content> getContents() {
		return contents;
	}
}
