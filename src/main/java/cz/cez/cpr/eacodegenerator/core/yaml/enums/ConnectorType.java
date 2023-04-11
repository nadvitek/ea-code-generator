package cz.cez.cpr.eacodegenerator.core.yaml.enums;

import java.util.stream.Stream;

public enum ConnectorType {

	ABSTRACTION("Abstraction"),
	AGGREGATION("Aggregation"),
	ASSOCIATION("Association"),
	CONTROL_FLOW("ControlFlow"),
	DEPENDENCY("Dependency"),
	GENERALIZATION("Generalization"),
	INFORMATION_FLOW("InformationFlow"),
	NOTELINK("NoteLink"),
	REALISATION("Realisation"),
	SEQUENCE("Sequence"),
	USAGE("Usage");

	private final String name;

	ConnectorType(String name) {
		this.name = name;
	}

	public static boolean isGeneralisation(String type) {
		return of(type) == GENERALIZATION;
	}

	public String getName() {
		return name;
	}

	public static ConnectorType of(String value) {
		return Stream.of(values()).filter(v -> v.name.equals(value)).findAny().orElse(null);
	}

	@Override
	public String toString() {
		return name;
	}

}
