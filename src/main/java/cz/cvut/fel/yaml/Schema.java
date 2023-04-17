package cz.cvut.fel.yaml;

import java.util.Comparator;

public abstract class Schema extends Component implements Comparable<Schema> {

	public static final String HACK_FOR_CLASS_NAME_CASE = "HackForClassNameCase";
	public static final String CLASS_NAME_CASE = "Case";

	protected String description;
	protected String name;

	public Schema(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		super(parent, srcCard, eaId, eaPath);
	}

	public Schema description(String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Schema name(String name) {
		this.name = getModel().isLdmMode() && CLASS_NAME_CASE.equals(name) ? HACK_FOR_CLASS_NAME_CASE : name;
		return this;
	}

	public String getName() {
		return name;
	}

	@Override
	public int compareTo(Schema o) {
		return Comparator.nullsFirst(Comparator.comparing(Schema::getName)).compare(this, o);
	}
}
