package cz.cez.cpr.eacodegenerator.core.yaml.enums;

public enum SchemaFormat {
	SCHEMA_FULL,
	SCHEMA_REF,
	NAME_FULL,
	NAME_REF;

	public boolean hasSchema() {
		return this == SCHEMA_FULL || this == SCHEMA_REF;
	}

	public boolean hasName() {
		return this == NAME_FULL || this == NAME_REF;
	}

	public boolean isFull() {
		return this == SCHEMA_FULL || this == NAME_FULL;
	}

	public boolean isRef() {
		return this == SCHEMA_REF || this == NAME_REF;
	}
}
