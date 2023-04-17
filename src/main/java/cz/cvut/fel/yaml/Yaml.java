package cz.cvut.fel.yaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Yaml {

	private final Map<Long, Model> modelMap = new HashMap<>();
	private final Map<Long, ComplexSchema> schemas = new HashMap<>();

	private final String packageCommon;
	private final List<String> allowedPackages = new ArrayList<>();
	private final List<String> ignoredPackages = new ArrayList<>();

	public Yaml(String packageCommon) {
		this.packageCommon = packageCommon;
	}

	public void add(Model model, long eaId) {
		modelMap.put(eaId, model);
	}

	public List<Model> getList() {
		return new ArrayList<>(modelMap.values());
	}

	public void put(ComplexSchema schema) {
		schemas.put(schema.getEaId(), schema);
	}

	public ComplexSchema getSchema(long id) {
		return schemas.get(id);
	}

	public List<Schema> getSchemas() {
		return new ArrayList<>(schemas.values());
	}

	public String getPackageCommon() {
		return packageCommon;
	}

	public Yaml addAllowedPackage(String allowedPackage) {
		if (allowedPackage == null) {
			return this;
		}
		this.allowedPackages.add(allowedPackage);
		return this;
	}

	public List<String> getAllowedPackages() {
		return allowedPackages;
	}

	public Yaml addIgnoredPackage(String ignoredPackage) {
		if (ignoredPackage == null) {
			return this;
		}
		this.ignoredPackages.add(ignoredPackage);
		return this;
	}

	public List<String> getIgnoredPackages() {
		return ignoredPackages;
	}
}
