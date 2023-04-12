package cz.cez.cpr.eacodegenerator.core.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Response extends Component implements Comparable<Response> {

	private static final Logger log = LoggerFactory.getLogger(Response.class);
	private String code = "200";
	private String description;
	private Content content;
	private String contentType;

	public Response(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	@Override
	public void joinToModel(Model model) {
		// Do nothing
	}

	public Content content(Content content) {
		this.content = content;
		return content;
	}

	public List<Content> getContents() {
		return content == null ? Collections.emptyList() : Collections.singletonList(content);
	}

	public Response code(String code) {
		this.code = code;
		descriptionBasedOnCode();
		return this;
	}

	public void descriptionBasedOnCode() {
		switch (code) {
			case "200":
				description = "Successful operation";
				break;
			case "204":
				description = "No Content";
				break;
			case "400":
				description = "Invalid ID Supplied";
				break;
			case "401":
				description = "Unauthorized";
				break;
			case "403":
				description = "Forbidden";
				break;
			case "404":
				description = "Not Found";
				break;
			case "405":
				description = "Validation exception";
				break;
			default:
				log.warn("Unknown response code: {}", code);
		}
	}

	public String getCode() {
		return code;
	}

	public Response contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public Response description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(Response o) {
		return Comparator.nullsFirst(Comparator.comparing(Response::getCode)).compare(this, o);
	}
}
