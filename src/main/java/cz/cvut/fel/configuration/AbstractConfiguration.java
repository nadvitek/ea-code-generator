package cz.cvut.fel.configuration;


/**
 * This class represents an abstract configuration
 * used during the generation
 */
public abstract class AbstractConfiguration {

	/**
	 * this is a path to an export folder
	 */
	private final String exportFolder = "export";

	public String getExportFolder() {
		return exportFolder;
	}
}
