package cz.cez.cpr.eacodegenerator.core.yaml;

import cz.cez.cpr.eacodegenerator.core.metamodel.TObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class Component {

	private static final Logger log = LoggerFactory.getLogger(Component.class);

	private final long eaId;
	private final String eaPath;
	private boolean expanded = false;
	private final SrcCard srcCard;

	protected Model model;
	protected Component parent;

	protected TObject target;

	public Component(Component parent, SrcCard srcCard, long eaId, String eaPath) {
		this.parent = parent;
		this.eaId = eaId;
		this.srcCard = srcCard;
		this.eaPath = eaPath;
		if (parent != null) {
			this.model = parent.getModel();
		}
		this.joinToModel(model);
	}

	public SrcCard getSrcCard() {
		return srcCard;
	}

	public String getEaPath() {
		return eaPath;
	}

	public Component target(TObject target) {
		this.target = target;
		return this;
	}

	public TObject resetTarget() {
		TObject tmp = target;
		target = null;
		return tmp;
	}

	public long getEaId() {
		return eaId;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded() {
		this.expanded = true;
		log.info("Set expanded of: {}", getSrcCard().getName());
	}

	public abstract void joinToModel(Model model);

	public boolean isMethod() {
		return this instanceof Method;
	}

	public boolean isModel() {
		return this instanceof Model;
	}

	public boolean isRequest() {
		return this instanceof Request;
	}

	public boolean isResponse() {
		return this instanceof Response;
	}

	public boolean isContent() {
		return this instanceof Content;
	}

	public boolean isProperty() {
		return this instanceof Property;
	}

	public boolean isSchema() {
		return this instanceof Schema;
	}

	public boolean isPrimitiveSchema() {
		return this instanceof PrimitiveSchema;
	}

	public boolean isComplexSchema() {
		return this instanceof ComplexSchema;
	}

	public boolean isRootResponse() {
		return this instanceof RootResponse;
	}

	public Method castToMethod() {
		return (Method) this;
	}

	public Model getModel() {
		return model;
	}

	public Request castToRequest() {
		return (Request) this;
	}

	public Response castToResponse() {
		return (Response) this;
	}

	public RootResponse castToRootResponse() {
		return (RootResponse) this;
	}

	public Content castToContent() {
		return (Content) this;
	}

	public Schema castToSchema() {
		return (Schema) this;
	}

	public Property castToProperty() {
		return (Property) this;
	}

	public PrimitiveSchema castToPrimitiveSchema() {
		return (PrimitiveSchema) this;
	}

	public ComplexSchema castToComplexSchema() {
		return (ComplexSchema) this;
	}

	public Component getParent() {
		return parent;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Component component = (Component) o;
		return eaId == component.eaId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eaId);
	}

	public Component requestScopePhase() {
		getModel().setRequestScopePhase(true);
		return this;
	}

	public Component responseScopePhase() {
		getModel().setRequestScopePhase(false);
		return this;
	}

	public Yaml getYaml() {
		return getModel().getYaml();
	}
}
