package cz.cez.cpr.eacodegenerator.core.yaml;

public class SrcCard {

	private final String type;
	private String name;

	public SrcCard(String name) {
		this(null, Schema.HACK_FOR_CLASS_NAME_CASE.equals(name) ? Schema.CLASS_NAME_CASE : name);
	}

	public SrcCard(String type, String name) {
		this.type = type != null ? type : "1";
		this.name = name;
	}

	public boolean isArray() {
		return type.endsWith("..*");
	}

	public boolean isRequired() {
		return type.startsWith("1");
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void childPrefix() {
		this.name = "child" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public void parentPrefix() {
		this.name = "parent" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}
}
