package cz.cez.cpr.eacodegenerator.core.yaml;

import cz.cez.cpr.eacodegenerator.core.yaml.enums.GenerationMode;
import cz.cez.cpr.eacodegenerator.core.yaml.enums.PrimitiveType;
import cz.cez.cpr.eacodegenerator.core.yaml.enums.Stereotype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static cz.cez.cpr.eacodegenerator.core.yaml.Tag.makeTag;

public class Model extends Component {

	private static final Logger log = LoggerFactory.getLogger(Model.class);

	private static final AtomicLong idSequence = new AtomicLong(0);
	public static final String Z_ROOT_COMPLEX_SCHEMA = "zRootComplexSchema";

	private String modelNameSuffix = "Dto";
	private String modelSpecificPackage = "dto";
	private String apiSpecificPackage = "controller";

	private final Map<Long, Method> methods = new HashMap<>();
	private final List<Tag> tags = new ArrayList<>();
	private final Map<Long, ComplexSchema> schemas = new HashMap<>();

	private final Yaml yaml;
	private final String name;

	private String title;
	private String description;
	private String minorVersion;
	private String mainVersion;
	private String openApiVersion = "3.0.3";

	private String groupId;
	private String artifactId;

	private boolean requestScopePhase;
	private boolean ignoreCycles;
	private GenerationMode generationMode = GenerationMode.API;

	private final Stack<Component> stack = new Stack<>();

	public Model(Yaml yaml, String name, long eaId, String eaPath) {
		super(null, null, eaId, eaPath);
		this.yaml = yaml;
		this.yaml.add(this, eaId);
		this.name = name;
	}

	public void pushToStack(Component component) {
		if (!stack.contains(component)) {
			stack.push(component);
		} else if (ignoreCycles) {
			component.setExpanded();
			log.warn("Cycle detected: {}{}", System.lineSeparator(), getLongPath());
		} else {
			stack.push(component);
			String longPath = getLongPath();
			String shortPath = getShortPath();
			log.error("Cycle detected: {}{}", System.lineSeparator(), longPath);
			throw new UnsupportedOperationException("Cycle detected: " + shortPath);
		}
		if (component.isComplexSchema()) {
			component.castToComplexSchema().setPath(getLongPath());
		}
	}

	public String getShortPath() {
		return stack.stream()
				.map(c -> {
					String[] path = c.getEaPath().split("/");
					return path[path.length - 1];
				})
				.collect(Collectors.joining(" -> "));
	}

	public String getLongPath() {
		return stack.stream().map(Component::getEaPath)
				.collect(Collectors.joining(System.lineSeparator() + "    -> "));
	}

	public void popFromStack() {
		stack.pop();
	}

	public String getName() {
		return name;
	}

	public String getArtifactId() {
		return artifactId != null ? artifactId :
				name.replaceFirst("CPR_", "")
						.replace("_", "-")
						.toLowerCase();
	}

	public String getGroupId() {
		return groupId != null ? groupId : "cz.cez.cpr.restapi";
	}

	public String getGitSubfolder() {
		return getArtifactId().toLowerCase() + "/" + getMainVersion();
	}

	public String getGitSwaggerName() {
		return getArtifactId().toLowerCase() + "_" + getVersion() + ".swagger.yaml";
	}

	public Model title(String title) {
		this.title = title;
		return this;
	}

	public Model description(String description) {
		this.description = description;
		return this;
	}

	public Model groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public Model artifactId(String artifactId) {
		this.artifactId = artifactId;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Model minorVersion(String minorVersion) {
		this.minorVersion = StringUtils.hasText(minorVersion) ? minorVersion : "0";
		return this;
	}

	public Model mainVersion(String mainVersion) {
		this.mainVersion = mainVersion;
		return this;
	}

	public Model ignoreCycles() {
		this.ignoreCycles = true;
		return this;
	}

	public String getVersion() {
		return mainVersion + "." + minorVersion;
	}

	public String getMainVersion() {
		return "v" + mainVersion.replaceAll("\\.", "_");
	}

	public Model openApiVersion(String openApiVersion) {
		this.openApiVersion = StringUtils.hasText(openApiVersion) ? openApiVersion : this.openApiVersion;
		return this;
	}

	public String getOpenApiVersion() {
		return openApiVersion;
	}

	@Override
	public void joinToModel(Model model) {
		super.model = this;
	}

	public Model modelSpecificPackage(String modelSpecificPackage) {
		this.modelSpecificPackage = modelSpecificPackage;
		return this;
	}

	public Model modelNameSuffix(String modelNameSuffix) {
		this.modelNameSuffix = modelNameSuffix;
		return this;
	}

	public String getModelSpecificPackage() {
		return getGroupId() + "." + getArtifactId() + "." + model.getMainVersion() + "." + modelSpecificPackage;
	}

	public String getApiSpecificPackage() {
		return getGroupId() + "." + getArtifactId() + "." + model.getMainVersion() + "." + apiSpecificPackage;
	}

	public String getModelNameSuffix() {
		return modelNameSuffix;
	}

	public List<Tag> getTags() {
		methods.values().forEach(this::collectTag);
		return tags;
	}

	public void addSchema(ComplexSchema schema) {
		yaml.put(schema);
		schemas.put(schema.getEaId(), schema);
	}

	public ComplexSchema getSchema(long id) {
		return yaml.getSchema(id);
	}

	public Method getMethod(long id) {
		return this.methods.get(id);
	}

	public void addMethod(Method method) {
		this.methods.put(method.getEaId(), method);
	}

	public Map<String, List<Method>> getMethods() {
		// 1. Group by path
		Map<String, List<Method>> map = methods.values().stream()
				.collect(Collectors.groupingBy(Method::getPath));

		// 2. Sort methods in group
		map.values().forEach(Collections::sort);

		// 3. Convert to LinkedList ordered by first method in group
		return map
				.entrySet().stream()
				.sorted(Comparator.comparing(e -> e.getValue().get(0)))
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(oldValue, newValue) -> oldValue,
						LinkedHashMap::new));
	}

	public List<Schema> getSchemas() {
		return schemas.values().stream().filter(s -> !Z_ROOT_COMPLEX_SCHEMA.equals(s.getName())).collect(Collectors.toList());
	}

	public List<Schema> getCommonSchemas() {
		return yaml.getPackageCommon() != null ? schemas.values().stream()
				.filter(s -> s.getEaPath().startsWith(yaml.getPackageCommon()))
				.collect(Collectors.toList()) : new ArrayList<>();
	}

	public void setRequestScopePhase(boolean requestScopePhase) {
		this.requestScopePhase = requestScopePhase;
	}

	public boolean isRequestScopePhase() {
		return requestScopePhase;
	}

	public Yaml getYaml() {
		return yaml;
	}

	public static ComplexSchema createDummyModel(Yaml yaml, String mainVersion, String minorVersion) {

		final String root = "root";

		Model model = new Model(yaml, root, nextSequence(), root)
				.title(root)
				.mainVersion(mainVersion)
				.minorVersion(minorVersion);
		model.pushToStack(model);

		Method method = new Method(model, new SrcCard(null, null), nextSequence(), null)
				.path("/" + root)
				.type(Stereotype.METHOD_GET)
				.summary(root)
				.description(root)
				.operationId(root);

		model.addMethod(method);

		Response response = new Response(method, new SrcCard(null, null), nextSequence(), null)
				.code("200")
				.description(root);

		RootResponse rootResponse = new RootResponse(method, new SrcCard(null, null), nextSequence(), null);
		rootResponse.response(response);

		method.rootResponse(rootResponse);

		Content content = new Content(method, new SrcCard(null, null), nextSequence(), null);
		response.content(content);

		ComplexSchema cs = new ComplexSchema(method, new SrcCard(null, null), nextSequence(), null)
				.name(Z_ROOT_COMPLEX_SCHEMA)
				.castToComplexSchema();

		return cs;
	}

	private void collectTag(Method method) {
		String tagName = makeTag(method.getPath()).substring(1);
		System.out.println(tagName);
		if (!tagCollected(tagName)) {
			tags.add(new Tag(tagName));
		}
	}

	private boolean tagCollected(String tagName) {
		return tags.stream().anyMatch(t -> t.getName().equals(tagName));
	}

	public String matchingTag(String path) {
		return tags.stream()
				.map(Tag::getName)
				.filter(path::contains)
				.findFirst()
				.orElse(" ");
	}

	private static long nextSequence() {
		return idSequence.decrementAndGet();
	}

	private String removeAccents(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return s;
	}

	private String createValidName(Set<String> names, String name) {
		name = name == null ? "undefined" : name;
		name = removeAccents(name.replace(" ", "")
				.replace("-", "")
				.replace("(", "_")
				.replace(")", "")
				.replace(".", "_")
				.replace("/", "_")
				.replace("„", "")
				.replace("“", ""));
		String newName = name;
		int postfix = 1;
		while (names.contains(newName.toLowerCase())) {
			newName = name + postfix++;
		}
		return newName;
	}

	public void ldmFix() {
		fixDuplicateSchemaNames();
		fixDuplicatePropertyNames();
		setDefaultProperty();
		fixMappings();
	}

	public void apiFix() {
		setDefaultProperty();
	}

	private void fixDuplicateSchemaNames() {
		Set<String> names = new HashSet<>();
		schemas.values().forEach(schema -> {
			String newName = createValidName(names, schema.getName());
			schema.name(newName);
			names.add(newName.toLowerCase());
		});
	}

	private void fixDuplicatePropertyNames() {
		schemas.values().forEach(schema -> {
			Set<String> names = new HashSet<>();
			schema.getProperties().forEach(p -> {
				String newName = createValidName(names, p.getSrcCard().getName());
				p.getSrcCard().setName(newName);
				names.add(newName.toLowerCase());
			});
		});
	}

	private void setDefaultProperty() {
		schemas.values().forEach(schema -> {
			schema.getProperties().forEach(p -> {
				if (p.getSchema().isPrimitiveSchema() && p.getSchema().castToPrimitiveSchema().getType() == null) {
					p.getSchema().castToPrimitiveSchema().type(PrimitiveType.STRING);
				}
			});
			if (schema.getProperties().isEmpty()) {
				Property virtualProperty = new Property(schema, new SrcCard("0..1", "$virtualProperty"), nextSequence(), "");
				virtualProperty.schema(new PrimitiveSchema(virtualProperty, new SrcCard("0..1", "src"), nextSequence(), "")
						.type(PrimitiveType.STRING));
				schema.property(virtualProperty);
			}
		});
	}

	public void fixMappings() {
		schemas.values().forEach(schema -> {
			Set<ComplexSchema> mappingSet =
					schema.getMappingSet().stream().map(Component::getEaId).map(schemas::get).collect(Collectors.toSet());
			schema.getMappingSet().clear();
			schema.getMappingSet().addAll(mappingSet);
		});
	}

	public Model generationMode(GenerationMode generationMode) {
		this.generationMode = generationMode;
		return this;
	}

	public boolean isLdmMode() {
		return generationMode == GenerationMode.LDM;
	}
}
