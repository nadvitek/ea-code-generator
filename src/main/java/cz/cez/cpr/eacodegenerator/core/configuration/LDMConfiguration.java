package cz.cez.cpr.eacodegenerator.core.configuration;

public class LDMConfiguration extends AbstractConfiguration {

	private String basePackage;
	private String ignoredPackage;
	private String groupId;
	private String artifactId;
	private String mainVersion;
	private String minorVersion;
	private String description;

	public String getBasePackage() {
		return basePackage;
	}

	public LDMConfiguration basePackage(String basePackage) {
		this.basePackage = basePackage;
		return this;
	}

	public String getIgnoredPackage() {
		return ignoredPackage;
	}

	public LDMConfiguration ignoredPackage(String ignoredPackage) {
		this.ignoredPackage = ignoredPackage;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public LDMConfiguration groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public LDMConfiguration artifactId(String artifactId) {
		this.artifactId = artifactId;
		return this;
	}

	public String getMainVersion() {
		return mainVersion;
	}

	public LDMConfiguration mainVersion(String mainVersion) {
		this.mainVersion = mainVersion;
		return this;
	}

	public String getMinorVersion() {
		return minorVersion;
	}

	public LDMConfiguration minorVersion(String minorVersion) {
		this.minorVersion = minorVersion;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public LDMConfiguration description(String description) {
		this.description = description;
		return this;
	}

	public String getGitSubfolder() {
		return groupId.replace(".", "/") + "/" + artifactId;
	}

	public String getGitSwaggerName() {
		return artifactId + "_" + mainVersion + "." + minorVersion + ".swagger.yaml";
	}
}
