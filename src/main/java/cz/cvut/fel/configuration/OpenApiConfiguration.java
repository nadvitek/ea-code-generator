package cz.cvut.fel.configuration;

/**
 * This class represents an OpenAPI
 * configuration
 */
public class OpenApiConfiguration extends AbstractConfiguration {

	/**
	 * name of the interface
	 */
	private final String interfaceName;
	/**
	 * name of the main version of the interface
	 */
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
