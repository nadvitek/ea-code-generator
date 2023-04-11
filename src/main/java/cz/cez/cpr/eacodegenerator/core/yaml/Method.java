package cz.cez.cpr.eacodegenerator.core.yaml;

import cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype.METHOD_DELETE;
import static cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype.METHOD_GET;
import static cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype.METHOD_POST;
import static cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype.METHOD_PUT;

public class Method extends Component implements Comparable<Method> {

	private final static List<Stereotype> ORDER = Arrays.asList(
			METHOD_GET,
			METHOD_POST,
			METHOD_PUT,
			METHOD_DELETE
	);

	private String path;
	private Stereotype type;
	private String summary;
	private String description;
	private String operationId;

	private Request request;
	private RootResponse rootResponse;

	public Method(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		model.addMethod(this);
	}

	@Override
	public void validate() {
		// TODO
	}

	public Method path(String path) {
		this.path = path;
		return this;
	}

	public String getPath() {
		return path;
	}

	public Method type(Stereotype type) {
		this.type = type;
		return this;
	}

	public Stereotype getType() {
		return type;
	}

	public Method summary(String summary) {
		this.summary = summary;
		return this;
	}

	public String getSummary() {
		return summary;
	}

	public Method description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Method operationId(String operationId) {
		this.operationId = operationId;
		return this;
	}

	public String getOperationId() {
		return operationId;
	}

	public Request request(Request request) {
		this.request = request;
		return request;
	}

	public Optional<Request> getRequest() {
		return request == null ? Optional.empty() : Optional.of(request);
	}

	public RootResponse rootResponse(RootResponse rootResponse) {
		this.rootResponse = rootResponse;
		return rootResponse;
	}

	public List<Response> getResponses() {
		return rootResponse == null ? new ArrayList<>() : rootResponse.getResponses();
	}

	@Override
	public int compareTo(Method method) {
		return Comparator
				.comparing(Method::order)
				.thenComparing(m -> m.getPath() != null ? m.getPath().length() : Integer.MAX_VALUE)
				.compare(this, method);
	}

	private static int order(Method m) {
		if (m == null || m.getType() == null) {
			return -1;
		}
		int order = ORDER.indexOf(m.getType());
		return order >= 0 ? order : Integer.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "Method{" +
				"type='" + type + '\'' +
				", path=" + path +
				'}';
	}
}
