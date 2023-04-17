package cz.cvut.fel.configuration;

public class OpenApiConfiguration extends AbstractConfiguration {

	private final String interfaceName;
	private final String interfaceMainVersion;

	public OpenApiConfiguration(String interfaceName, String interfaceMainVersion) {
		this.interfaceName = interfaceName;
		this.interfaceMainVersion = interfaceMainVersion;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getInterfaceMainVersion() {
		return interfaceMainVersion;
	}
}
