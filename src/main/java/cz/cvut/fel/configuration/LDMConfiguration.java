package cz.cvut.fel.configuration;

import lombok.Getter;

/**
 * This class represents and LDM
 * (Logický datový model) configuration
 */
@Getter
public class LDMConfiguration extends AbstractConfiguration {

	private String basePackage;
	private String ignoredPackage;
	private String mainVersion;
	private String minorVersion;
	private String description;

	public LDMConfiguration basePackage(String basePackage) {
		System.out.println(basePackage);
		this.basePackage = basePackage;
		return this;
	}

	public LDMConfiguration ignoredPackage(String ignoredPackage) {
		this.ignoredPackage = ignoredPackage;
		return this;
	}

	public LDMConfiguration mainVersion(String mainVersion) {
		this.mainVersion = mainVersion;
		return this;
	}

	public LDMConfiguration minorVersion(String minorVersion) {
		this.minorVersion = minorVersion;
		return this;
	}

	public LDMConfiguration description(String description) {
		this.description = description;
		return this;
	}
}
