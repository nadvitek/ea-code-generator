package cz.cvut.fel.generator;

import cz.cvut.fel.configuration.AbstractConfiguration;
import cz.cvut.fel.configuration.LDMConfiguration;
import cz.cvut.fel.configuration.OpenApiConfiguration;
import cz.cvut.fel.metamodel.TAttribute;
import cz.cvut.fel.metamodel.TConnector;
import cz.cvut.fel.metamodel.TObject;
import cz.cvut.fel.metamodel.TPackage;
import cz.cvut.fel.repository.TObjectRepository;
import cz.cvut.fel.repository.TPackageRepository;
import cz.cvut.fel.service.YamlToStringService;
import cz.cvut.fel.util.validations.Validations;
import cz.cvut.fel.yaml.ComplexSchema;
import cz.cvut.fel.yaml.Component;
import cz.cvut.fel.yaml.Content;
import cz.cvut.fel.yaml.Method;
import cz.cvut.fel.yaml.Model;
import cz.cvut.fel.yaml.Parameter;
import cz.cvut.fel.yaml.PrimitiveSchema;
import cz.cvut.fel.yaml.Property;
import cz.cvut.fel.yaml.Request;
import cz.cvut.fel.yaml.Response;
import cz.cvut.fel.yaml.RootResponse;
import cz.cvut.fel.yaml.Schema;
import cz.cvut.fel.yaml.SrcCard;
import cz.cvut.fel.yaml.Yaml;
import cz.cvut.fel.yaml.enums.ConnectorType;
import cz.cvut.fel.yaml.enums.GenerationMode;
import cz.cvut.fel.yaml.enums.ObjectType;
import cz.cvut.fel.yaml.enums.PrimitiveType;
import cz.cvut.fel.yaml.enums.PrimitiveTypeFormat;
import cz.cvut.fel.yaml.enums.Stereotype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cz.cvut.fel.util.validations.Validations.ENUM;
import static cz.cvut.fel.util.validations.Validations.TYPE;
import static cz.cvut.fel.util.validations.Validations.V_API_CONTENT_TYPE;
import static cz.cvut.fel.util.validations.Validations.V_CONNECTOR_NAME;
import static cz.cvut.fel.util.validations.Validations.V_DESCRIPTION;
import static cz.cvut.fel.util.validations.Validations.V_EXAMPLE;
import static cz.cvut.fel.util.validations.Validations.V_INVALID_SCHEMA;
import static cz.cvut.fel.util.validations.Validations.V_OBJECT_NAME;
import static cz.cvut.fel.util.validations.Validations.V_OPEN_API_VERSION;
import static cz.cvut.fel.util.validations.Validations.V_PARAMETER_STEREOTYPE;
import static cz.cvut.fel.util.validations.Validations.V_PRIMITIVE_TYPE;
import static cz.cvut.fel.util.validations.Validations.V_PRIMITIVE_TYPE_FORMAT;
import static cz.cvut.fel.util.validations.Validations.V_PRIMITIVE_TYPE_FORMAT_VALID;
import static cz.cvut.fel.util.validations.Validations.V_PRIMITIVE_TYPE_VALID;
import static cz.cvut.fel.util.validations.Validations.V_VERSION;

@org.springframework.stereotype.Component
public class SwaggerGenerator extends AbstractGenerator {

	private static final Logger log = LoggerFactory.getLogger(SwaggerGenerator.class);

	public static final String CODEBOOK_ENTRY = "CodebookEntry";
	public static final String CODEBOOK_ENTRY_IDENTIFIER = "CodebookEntryIdentifier";

	private final boolean logDetail;

	@Value("${ea.shared.dto.version}")
	private String sharedDtoVersion;

	@Value("${ea.codebook.entry.guid}")
	public String codebookEntryGuid;

	@Value("${ea.codebook.entry.identifier.guid}")
	public String codebookEntryIdentifierGuid;

	@Value("${ea.pageableobjectrequest.guid}")
	public String pageableObjectRequestGuid;

	private final String packageCommon;
	private final YamlToStringService yamlToStringService;
	private final TObjectRepository objectRepository;

	public SwaggerGenerator(
			TPackageRepository tPackageRepository,
			YamlToStringService yamlToStringService,
			TObjectRepository objectRepository,
			@Value("${ea.log.detail:@null}") String logDetail,
			@Value("${ea.package.common}") String packageCommon) throws IOException {
		super(tPackageRepository);
		this.logDetail = "true".equals(logDetail);
		this.yamlToStringService = yamlToStringService;
		this.objectRepository = objectRepository;
		this.packageCommon = decodeUrl(packageCommon);
	}

	@Transactional(readOnly = true)
	public void generate(LDMConfiguration conf) {

		log.info("**************** RUNNING CONFIGURATION ****************");
		log.info("Base package:    {}", conf.getBasePackage());
		log.info("Ignored package: {}", conf.getIgnoredPackage());
		log.info("Artifact ID:     {}", conf.getArtifactId());
		log.info("Group ID:        {}", conf.getGroupId());
		log.info("Main version:    {}", conf.getMainVersion());
		log.info("Minor version:   {}", conf.getMinorVersion());
		log.info("Description:     {}", conf.getDescription());
		log.info("Log detail:      {}", logDetail);
		log.info("*******************************************************");

		cleanFolder(conf.getExportFolder());

		Yaml yaml = new Yaml(null)
				.addAllowedPackage(conf.getBasePackage())
				.addIgnoredPackage(conf.getIgnoredPackage());
		ComplexSchema schema = Model.createDummyModel(yaml, conf.getMainVersion(), conf.getMinorVersion());
		schema.getModel()
				.title(conf.getDescription())
				.mainVersion(conf.getMainVersion())
				.minorVersion(conf.getMinorVersion())
				.groupId(conf.getGroupId())
				.artifactId(conf.getArtifactId())
				.ignoreCycles()
				.modelSpecificPackage("domain")
				.modelNameSuffix(null)
				.generationMode(GenerationMode.LDM);

		TPackage packageByPath = getPackageByPath(conf.getBasePackage());
		Assert.notNull(packageByPath, "!!! PACKAGE NOT FOUND !!!");

		processPackage(packageByPath, schema);
		schema.getModel().ldmFix();

		write(yamlToStringService.toString(schema.getModel()), "swagger.yaml", conf.getExportFolder());
		write(yamlToStringService.toStringJavaConfig(schema.getModel()), "java-config.yaml", conf.getExportFolder());
		write(readTemplate("git_push_template.sh")
						.replace("${swagger.folder}", conf.getGitSubfolder())
						.replace("${swagger.name}", conf.getGitSwaggerName())
						.replace("${commit.message}", conf.getGitSwaggerName()),
				"git_push.sh", conf.getExportFolder());
		write(readTemplate("pom-ldm-template.xml")
						.replace("${groupId}", schema.getModel().getGroupId())
						.replace("${artifactId}", schema.getModel().getArtifactId())
						.replace("${version}", schema.getModel().getVersion())
						.replace("${description}", schema.getModel().getTitle()),
				"pom.xml", conf.getExportFolder());
	}

	private void processPackage(TPackage tPackage, ComplexSchema parent) {
		packageRepository.findTPackageByParentId(tPackage.getId()).forEach(p -> processPackage(p, parent));
		objectRepository.findByTpackageAndType(tPackage, "Class").stream()
				.filter(o -> parent.getModel().getSchema(o.getId()) == null)
				.filter(o -> !isIgnored(parent.getYaml(), o))
				.forEach(tObject -> {
					log.info("Processing Class: {}", tObject.getName());
					Property property = new Property(parent, new SrcCard("0..1", "_" + tObject.getName()), tObject.getId(), "");
					parent.property(property);
					ComplexSchema schema = new ComplexSchema(property, new SrcCard("0..1", "src"), tObject.getId(), getEaPath(tObject))
							.name(tObject.getName()).castToComplexSchema();
					if (!schema.isExpanded()) {
						parent.getModel().pushToStack(schema);
						processGeneralization(tObject, schema);
						processConnectors(tObject, schema);
						property.schema(schema);
						parent.getModel().popFromStack();
						schema.setExpanded();
					} else {
						log.info("Do nothing - schema has been expanded!");
					}
				});
	}

	private boolean isIgnored(Yaml yaml, TObject tObject) {
		String eaPath = getEaPath(tObject);
		if (eaPath == null) {
			return false;
		}
		return (!yaml.getAllowedPackages().isEmpty() && yaml.getAllowedPackages().stream().noneMatch(eaPath::startsWith))
				|| yaml.getIgnoredPackages().stream().anyMatch(eaPath::startsWith);
	}

	@Transactional(readOnly = true)
	public void generate(OpenApiConfiguration conf) {

		log.info("**************** RUNNING CONFIGURATION ****************");
		log.info("Interface:      {}", conf.getInterfaceName());
		log.info("Main version:   {}", conf.getInterfaceMainVersion());
		log.info("Common package: {}", packageCommon);
		log.info("Log detail:     {}", logDetail);
		log.info("*******************************************************");

		cleanFolder(conf.getExportFolder());

		TObject anInterface = getInterface(conf);

		Yaml yaml = new Yaml(packageCommon);
		processOneInterface(anInterface, yaml);
		yaml.getList().forEach(m -> writeAll(conf, m));
	}

	private void writeAll(AbstractConfiguration conf, Model m) {
		m.apiFix();
		write(yamlToStringService.toString(m), "swagger.yaml", conf.getExportFolder());
		write(yamlToStringService.toStringJavaConfig(m), "java-config.yaml", conf.getExportFolder());
		write(yamlToStringService.toStringTsConfig(m), "ts-config.yaml", conf.getExportFolder());

		write(readTemplate("index-template"), "index.ts", conf.getExportFolder());

		write(readTemplate("package-template.json")
						.replace("${name}", String.format("@%s/%s", m.getGroupId(), m.getArtifactId()))
						.replace("${version}", m.getVersion())
						.replace("${description}", m.getTitle())
						.replace("${shared.dto.version}", sharedDtoVersion),
				"package.json", conf.getExportFolder());

		write(readTemplate("pom-template.xml")
						.replace("${groupId}", m.getGroupId())
						.replace("${artifactId}", m.getArtifactId())
						.replace("${version}", m.getVersion())
						.replace("${description}", m.getTitle())
						.replace("${shared.dto.version}", sharedDtoVersion),
				"pom.xml", conf.getExportFolder());

		write(readTemplate("git_push_template.sh")
						.replace("${swagger.folder}", m.getGitSubfolder())
						.replace("${swagger.name}", m.getGitSwaggerName())
						.replace("${commit.message}", m.getGitSwaggerName()),
				"git_push.sh", conf.getExportFolder());
	}

	private TObject getInterface(OpenApiConfiguration conf) {
		Map<String, TObject> interfaces = objectRepository.findByTypeAndName("Interface", conf.getInterfaceName())
				.collect(Collectors.toMap((TObject i) -> i.getTpackage().getName(), Function.identity()));

		TObject anInterface = interfaces.get(conf.getInterfaceMainVersion());
		Assert.notNull(anInterface,
				String.format("Can't found interface %s in main version %s. This interface exists in this versions: %s",
						conf.getInterfaceName(), conf.getInterfaceMainVersion(),
						interfaces.keySet().stream().sorted().collect(Collectors.toList())));
		return anInterface;
	}

	private void processOneInterface(TObject object, Yaml yaml) {
		log(object);
		if (logDetail) {
			log.info("Interface: {}", object);
		}
		Model model = new Model(yaml, object.getName(), object.getId(), getEaPath(object))
				.title(object.getName())
				.description(object.getNote())
				.minorVersion(V_VERSION.valid(object))
				.mainVersion(object.getTpackage().getName())
				.openApiVersion(V_OPEN_API_VERSION.valid(object))
				.ignoreCycles();
		model.pushToStack(model);
		object.getEndConns().stream()
				.filter(c -> Stereotype.isMethodStereotype(target(c).getStereotype()) && c.getName() != null)
				.forEach(c -> processConnector(c, model));
	}

	private void processConnector(TConnector conn, Component cParent) {
		if (conn == null || cParent == null) {
			return;
		}

		log.info("--------------------------------------------------------------------");
		log.info("Current stack: {}", cParent.getModel().getShortPath());
		log.info("Phase: {}", cParent.getModel().isRequestScopePhase() ? "REQUEST" : "RESPONSE");
		logConnector(conn, "Processing connector", logDetail);

		TObject target = target(conn);
		Stereotype tStereotype = Stereotype.of(target.getStereotype());
		Stereotype cStereotype = Stereotype.of(conn.getStereotype());
		ObjectType tType = ObjectType.of(target.getType());

		if (isIgnored(cParent.getYaml(), target)) {
			log.info("IGNORE: {}", target.getName());
			return;
		}

		Component newComp = null;
		boolean processPageableRequest = false;
		if (cParent.isModel() && Stereotype.isMethodStereotype(tStereotype)) {
			newComp = createMethod(conn, cParent);
		} else if (cParent.isMethod() && tStereotype == Stereotype.REQUEST) {
			newComp = cParent.requestScopePhase()
					.castToMethod()
					.request(createRequest(conn, cParent));
			processPageableRequest = true;
		} else if (cParent.isMethod() && tStereotype == Stereotype.RESPONSE) {
			newComp = cParent.responseScopePhase()
					.castToMethod()
					.rootResponse(createRootResponse(conn, cParent));
		} else if (cParent.isRequest() && (cStereotype == Stereotype.API_PATH || cStereotype == Stereotype.API_QUERY)) {
			cParent.castToRequest()
					.parameter(createParameter(conn, cParent));
		} else if (cParent.isRequest() && cStereotype == Stereotype.API_BODY) {
			newComp = cParent.castToRequest()
					.content(createContent(conn, cParent))
					.schema(createSchema(conn, target(conn), cParent));
		} else if (cParent.isRootResponse() && tStereotype == Stereotype.RESPONSE) {
			newComp = cParent.responseScopePhase()
					.castToRootResponse()
					.response(createResponse(conn, cParent));
		} else if (cParent.isResponse() && (tType == ObjectType.PRIMITIVE_TYPE || tType == ObjectType.CLASS)) {
			newComp = cParent.castToResponse()
					.content(createContent(conn, cParent))
					.schema(createSchema(conn, target(conn), cParent));
		} else if (cParent.isComplexSchema() && (tType == ObjectType.PRIMITIVE_TYPE || tType == ObjectType.CLASS)
				&& !ConnectorType.isGeneralisation(conn.getType())) {
			newComp = createPropertySchema(cParent.castToComplexSchema(), conn);
		} else {
			log.info("Do nothing!");
			return;
		}

		if (newComp != null && !newComp.isExpanded()) {
			newComp.getModel().pushToStack(newComp);
		}

		if (newComp == null || newComp.isExpanded()) {
			return;
		}

		TObject newTarget = newComp.resetTarget();
		if (newTarget != null) {
			target = newTarget;
		}

		processGeneralization(target, newComp);
		processConnectors(target, newComp);
		if (processPageableRequest) {
			processPageableRequest(target, newComp);
		}

		newComp.setExpanded();
		newComp.getModel().popFromStack();
	}

	private void processConnectors(TObject target, Component component) {
		log.info("Processing connectors of: {}", target.getName());
		List<TConnector> endConns = target.getEndConns();
		log.info("Detected {} connections:", endConns.size());
		AtomicInteger count = new AtomicInteger(1);
		endConns.forEach(conn -> logConnector(conn, String.format("- Connector[%02d]", count.getAndIncrement()), false));
		endConns.forEach(c -> processConnector(c, component));
	}

	private void processPageableRequest(TObject object, Component component) {
		TConnector pageable = getGeneralization(object, pageableObjectRequestGuid);
		if (pageable == null) {
			log.info("Paging not detected for object: {}", object.getName());
			return;
		}
		TObject pageableObjectRequest = pageable.getEndObject();
		log.info("Processing paging of: {}", object.getName());
		processConnectors(pageableObjectRequest, component);
	}

	private void processGeneralization(TObject object, Component component) {
		if (!component.isComplexSchema()) {
			log.info("Processing generalization of {} will be skipped since component is not a complex schema!", object.getName());
			return;
		}
		log.info("Processing generalization of: {}", object.getName());
		TConnector generalization = getGeneralization(object);
		if (generalization == null) {
			log.info("There is no generalization of: {}", object.getName());
			return;
		}
		ComplexSchema child = component.castToComplexSchema();
		TObject target = generalization.getEndObject();
		log.info("Generalization of {} detected: {} extends {}", child.getName(), child.getName(), target.getName());
		Schema parent = createSchema(generalization, target, component.getModel());
		if (parent.isComplexSchema()) {
			child.allOf(parent.castToComplexSchema());
			parent.castToComplexSchema().mapping(child.castToComplexSchema());
		}
		if (parent.isExpanded()) {
			return;
		}
		component.getModel().pushToStack(parent);
		parent.setExpanded();

		processGeneralization(target, parent);
		processConnectors(target, parent);

		component.getModel().popFromStack();
	}

	private void logConnector(TConnector conn, String msg, boolean logStartObject) {
		TObject endObject = conn.getEndObject();
		TObject startObject = conn.getStartObject();
		log.info("{} (type: {}, name: {}): {} --- <<{}>> ---> {}",
				msg,
				conn.getType(),
				conn.getName(),
				endObject.getName() != null ? endObject.getName() : endObject.getEaGuid(),
				conn.getStereotype(),
				startObject.getName() != null ? startObject.getName() : startObject.getEaGuid());
		if (logStartObject) {
			log.info("Processing object: {}", startObject);
		}
	}

	private Component createPropertySchema(ComplexSchema cParent, TConnector conn) {

		TObject target = target(conn);
		log.info("Creating property of: {}", target.getName());

		TObject association = conn.getPdata1();
		if (association == null) {
			log.info("Association does not detected");
			return cParent.property(new Property(cParent, srcCard(conn), target.getId(), getEaPath(target)))
					.schema(createSchema(conn, target(conn), cParent));
		}

		log.info("Association detected: {}", association.getName() != null);

		log.info("Association({}): create property", association.getName());
		Property associationProperty = cParent.property(
				new Property(cParent, new SrcCard("1..*", association.getName()), association.getId(), getEaPath(association)));

		log.info("Association({}): create property", association.getName());
		ComplexSchema associationSchema = createComplexSchema(association, new SrcCard("1..*", association.getName()), associationProperty);
		associationProperty.schema(associationSchema);

		log.info("Association({}): creating child/parent field", association.getName());
		SrcCard srcCardParent = new SrcCard(cParent.castToComplexSchema().getName());
		SrcCard srcCardChild = srcCard(conn);
		if (srcCardParent.getName().equals(srcCardChild.getName())) {
			srcCardParent.parentPrefix();
			srcCardChild.childPrefix();
		}

		log.info("Association({}): paren property: {}, child property: {}",
				association.getName(), srcCardParent.getName(), srcCardChild.getName());
		Property parentProperty = new Property(associationSchema, srcCardParent, target.getId(), getEaPath(target));
		associationSchema.property(parentProperty).schema(cParent.castToSchema());
		Property childProperty = associationSchema.property(new Property(associationSchema, srcCardChild, target.getId(), getEaPath(target)));

		log.info("Association({}): expanding...", association.getName());

		associationSchema.getModel().pushToStack(associationSchema);
		processGeneralization(association, associationSchema);
		processConnectors(association, associationSchema);
		associationSchema.setExpanded();
		associationSchema.getModel().popFromStack();

		log.info("Association({}): created!", association.getName());

		return childProperty.schema(createSchema(conn, target(conn), cParent));
	}

	private SrcCard srcCard(TConnector conn) {
		return new SrcCard(conn.getSrcCard(), StringUtils.hasText(conn.getName()) ? conn.getName() : target(conn).getName());
	}

	private RootResponse createRootResponse(TConnector conn, Component cParent) {
		TObject target = target(conn);
		log.info("Creating root response of: {}", target.getName());
		return new RootResponse(cParent, srcCard(conn), target.getId(), getEaPath(target));
	}

	private Content createContent(TConnector conn, Component cParent) {
		TObject target = target(conn);
		log.info("Creating content of: {}", target.getName());
		return new Content(cParent, srcCard(conn), target.getId(), getEaPath(target));
	}

	private Schema createSchema(TConnector conn, TObject target, Component cParent) {

		log(target);
		SrcCard srcCard = srcCard(conn);
		TObject typeObject = getTypeObject(target);
		ObjectType objectType = ObjectType.of(target.getType());

		log.info("Creating schema of: {}, typeObject: {}, objectType={}, stereotype={}",
				target.getName(),
				typeObject == null ? null : typeObject.getEaGuid(),
				objectType,
				target.getStereotype());

		if (objectType == ObjectType.CLASS) {
			return createComplexSchema(target, srcCard, cParent);
		} else if (objectType == ObjectType.PRIMITIVE_TYPE && typeObject == null) {
			return createPrimitiveSchema(target, srcCard, cParent);
		} else if (objectType == ObjectType.PRIMITIVE_TYPE) {
			log.info("Creating complex schema from primitive: {}", target.getName());
			return createComplexSchema(typeObject, srcCard, cParent);
		} else {
			return V_INVALID_SCHEMA.valid(objectType);
		}
	}

	private TObject getCodebookEntry(boolean requestScope) {
		return requestScope ? findObject(CODEBOOK_ENTRY_IDENTIFIER, codebookEntryIdentifierGuid)
				: findObject(CODEBOOK_ENTRY, codebookEntryGuid);
	}

	private TObject findObject(String name, String guid) {
		return objectRepository.findByNameAndEaGuid(name, String.format("{%s}", guid)).orElseThrow(() ->
				new IllegalArgumentException(String.format("There is no TObject of name: %s, eaGuid: %s", name, guid)));
	}

	private ComplexSchema createComplexSchema(TObject target, SrcCard srcCard, Component cParent) {

		ComplexSchema schema = cParent.getModel().getSchema(target.getId());
		Stereotype stereotype = Stereotype.of(target.getStereotype());

		log.info("Creating complex schema of: {}, stereotype: {}", schema, stereotype);

		if (stereotype == Stereotype.CODEBOOK) {
			TObject codebook = getCodebookEntry(cParent.getModel().isRequestScopePhase());
			log.info("Object {} will be replaced with {}", target.getName(), codebook.getName());
			target = codebook;
		}

		if (schema == null) {
			log.info("Creating new complex schema of: {}", target.getName());
			schema = new ComplexSchema(cParent, srcCard, target.getId(), getEaPath(target));
		} else {
			log.info("Loaded complex schema: {}, expanded: {}", target.getName(), schema.isExpanded());
		}

		schema
				.description(description(target))
				.name(name(target))
				.target(target);
		return schema;
	}

	private Schema createPrimitiveSchema(TObject target, SrcCard srcCard, Component cParent) {
		TObject enumObject = getEnumObject(target);
		PrimitiveType type = primitiveType(target);
		log.info("Creating primitive schema of: {}, type: {}, enumObject: {}",
				target.getName(),
				type,
				enumObject == null ? null : enumObject.getEaGuid());
		PrimitiveSchema schema = new PrimitiveSchema(cParent, srcCard, target.getId(), getEaPath(target))
				.example(V_EXAMPLE.valid(target))
				.type(type)
				.format(primitiveTypeFormat(target))
				.description(description(target))
				.name(name(target))
				.castToPrimitiveSchema();
		if (type == PrimitiveType.ENUM && enumObject == null) {
			throw new IllegalStateException(
					String.format("Enumeration %s requires attribute %s!", target.getName(), Validations.ENUM));
		} else if (type == PrimitiveType.ENUM) {
			log.info("ENUM TYPE:");
			log.info("{}", enumObject);
			enumObject.getAttributes().forEach(a -> schema.addEnumValue(a.getName()));
		}
		return schema;
	}

	private Parameter createParameter(TConnector conn, Component cParent) {
		TObject target = log(target(conn));
		log.info("Creating parameter of: {}", target.getName());
		String name = StringUtils.hasText(conn.getName()) ? conn.getName() : V_OBJECT_NAME.valid(target);
		return new Parameter(cParent, srcCard(conn), target.getId(), getEaPath(target))
				.in(V_PARAMETER_STEREOTYPE.valid(conn))
				.name(name)
				.schema(createSchema(conn, target(conn), cParent))
				.example(V_EXAMPLE.valid(target))
				.description(description(target));
	}

	private Method createMethod(TConnector conn, Component parentC) {
		TObject target = log(target(conn));
		log.info("Creating method of: {}", target.getName());
		Method method = parentC.getModel().getMethod(target.getId());
		return method != null ? method :
				new Method(parentC, srcCard(conn), target.getId(), getEaPath(target))
						.path(V_CONNECTOR_NAME.valid(conn))
						.type(Stereotype.of(target.getStereotype()))
						.summary(V_OBJECT_NAME.valid(target))
						.description(description(target));
	}

	private Request createRequest(TConnector conn, Component cParent) {
		TObject target = log(target(conn));
		log.info("Creating request of: {}", target.getName());
		return new Request(cParent, srcCard(conn), target.getId(), getEaPath(target))
				.contentType(V_API_CONTENT_TYPE.valid(target));
	}

	private Response createResponse(TConnector conn, Component cParent) {
		TObject target = log(target(conn));
		log.info("Creating response of: {}", target.getName());
		return new Response(cParent, srcCard(conn), target.getId(), getEaPath(target))
				.code(target.getName().substring(9))
				.contentType(V_API_CONTENT_TYPE.valid(target));
	}

	private TObject target(TConnector conn) {
		return conn.getStartObject();
	}

	public String description(TObject o) {
		return V_DESCRIPTION.valid(o);
	}

	private String name(TObject target) {
		return target.getName() == null ? "undefined" : target.getName().replaceAll(" ", "");
	}

	public PrimitiveType primitiveType(TObject o) {
		String type = V_PRIMITIVE_TYPE.valid(o);
		return V_PRIMITIVE_TYPE_VALID.valid(type);
	}

	public PrimitiveTypeFormat primitiveTypeFormat(TObject o) {
		String type = V_PRIMITIVE_TYPE_FORMAT.valid(o);
		return V_PRIMITIVE_TYPE_FORMAT_VALID.valid(type);
	}

	public TObject getTypeObject(TObject o) {
		return getTypeObject(o, TYPE);
	}

	public TObject getEnumObject(TObject o) {
		return getTypeObject(o, ENUM);
	}

	public TObject getTypeObject(TObject o, String attributeName) {
		TAttribute attribute = o.getAttributeByName(attributeName);
		return attribute == null || attribute.getTypeObject() == null || attribute.getTypeObject().getName() == null
				? null : attribute.getTypeObject();
	}

	public TConnector getGeneralization(TObject o) {
		return getGeneralization(o, null);
	}

	public TConnector getGeneralization(TObject o, String guid) {
		return getGeneralizations(o, guid);
	}

	public TConnector getGeneralizations(TObject o, String guid) {
		return o.getSourceConns().stream()
				.filter(c -> ConnectorType.isGeneralisation(c.getType()) && c.getEndObject() != null)
				.filter(c -> guid == null || ("{" + guid + "}").equals(c.getEndObject().getEaGuid()))
				.findAny().orElse(null);
	}
}
