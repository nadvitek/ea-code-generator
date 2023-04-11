package cz.cez.cpr.eacodegenerator.core.metamodel;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "t_object")
@AttributeOverride(column = @Column(name = "Object_ID"), name = "id")
public class TObject extends AbstractEntity {

	private static final String BR = "\n";

	@Column(name = "Object_Type")
	private String type;

	@Column(name = "Name")
	private String name;

	@Column(name = "Note")
	private String note;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "Package_ID")
	private TPackage tpackage;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "ParentID")
	private TObject parent;

	@Column(name = "Stereotype")
	private String stereotype;

	@Column(name = "Abstract")
	private Boolean abstractObj;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "Start_Object_ID")
	private List<TConnector> sourceConns;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "End_Object_ID")
	private List<TConnector> endConns;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "Object_ID")
	private List<TAttribute> attributes;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "Object_ID")
	private List<TObjectProperty> properties;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "Object_ID")
	private List<TOperation> operations;

	@Column(name = "ea_guid")
	private String eaGuid;

	@Column(name = "alias")
	private String alias;

	public String getNameFirstLower() {
		return StringUtils.uncapitalize(name);
	}

	public String getNameFirstUpper() {
		return StringUtils.capitalize(name);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setTpackage(TPackage tpackage) {
		this.tpackage = tpackage;
	}

	public TPackage getTpackage() {
		return tpackage;
	}

	public void setParent(TObject parent) {
		this.parent = parent;
	}

	public TObject getParent() {
		return parent;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public String getStereotype() {
		return stereotype;
	}

	public Boolean getAbstractObj() {
		return abstractObj;
	}

	public void setAbstractObj(Boolean abstractObj) {
		this.abstractObj = abstractObj;
	}

	public void setSourceConns(List<TConnector> sourceConns) {
		this.sourceConns = sourceConns;
	}

	public List<TConnector> getSourceConns() {
		return sourceConns;
	}

	public void setEndConns(List<TConnector> endConns) {
		this.endConns = endConns;
	}

	public List<TConnector> getEndConns() {
		return endConns;
	}

	public void setAttributes(List<TAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<TAttribute> getAttributes() {
		return attributes;
	}

	public void setProperties(List<TObjectProperty> properties) {
		this.properties = properties;
	}

	public List<TObjectProperty> getProperties() {
		return properties;
	}

	public List<TOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<TOperation> operations) {
		this.operations = operations;
	}

	public String getEaGuid() {
		return eaGuid;
	}

	public void setEaGuid(String eaGuid) {
		this.eaGuid = eaGuid;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String toString() {
		return "TObject:"
				+ BR + "eaGuid:      " + eaGuid
				+ BR + "name:        " + name
				+ BR + "type:        " + type
				+ BR + "parent:      " + (parent != null ? parent.getEaGuid() : null)
				+ BR + "stereotype:  " + stereotype
				+ BR + "abstractObj: " + abstractObj
				+ BR + "tpackage:    " + toStringPackage(tpackage)
				+ BR + "sourceConns: " + toStringConnectors(sourceConns, TConnector::getEndObject)
				+ BR + "endConns:    " + toStringConnectors(endConns, TConnector::getStartObject)
				+ BR + "attributes:  " + toStringAttributes(attributes)
				+ BR + "properties:  " + toStringProperties(properties)
				+ BR + "operations:  " + operations
				+ BR + "alias:       " + alias;
	}

	private String toStringConnectors(List<TConnector> connectors, Function<TConnector, TObject> fn) {
		return connectors.stream().map(c -> toStringConnector(c, fn.apply(c)))
				.collect(Collectors.joining(BR + "    --------------------------------------------------"));
	}

	private String toStringConnector(TConnector c, TObject object) {
		return ""
				+ BR + "    object.eaGuid: " + (object != null ? object.getEaGuid() : null)
				+ BR + "    object.name:   " + (object != null ? object.getName() : null)
				+ BR + "    type:          " + c.getType()
				+ BR + "    stereotype:    " + c.getStereotype()
				+ BR + "    name:          " + c.getName()
				+ BR + "    srcCard:       " + c.getSrcCard()
				+ BR + "    destCard:      " + c.getDestCard()
				+ BR + "    destRole:      " + c.getDestRole()
				+ BR + "    direction:     " + c.getDirection()
				+ BR + "    srcRole:       " + c.getSrcRole()
				+ BR + "    id:            " + c.getId()
				+ BR + "    pdata1:        " + (c.getPdata1() == null ? null : c.getPdata1().getName())
				;
	}

	private String toStringAttributes(List<TAttribute> attributes) {
		return attributes.stream().map(this::toStringAttribute)
				.collect(Collectors.joining(BR + "    --------------------------------------------------"));
	}

	private String toStringAttribute(TAttribute attribute) {
		return ""
				+ BR + "    type:       " + attribute.getType()
				+ BR + "    name:       " + attribute.getName()
				+ BR + "    initValue:  " + attribute.getInitValue()
				+ BR + "    lowerBound: " + attribute.getLowerBound()
				+ BR + "    stereotype: " + attribute.getStereotype()
				+ BR + "    style:      " + attribute.getStyle()
				+ BR + "    upperBound: " + attribute.getUpperBound()
				+ BR + "    classifier: " + attribute.getClassifier()
				+ BR + "    typeObject: " + (attribute.getTypeObject() == null ? null : ""
				+ BR + "        eaGuid: " + attribute.getTypeObject().getEaGuid()
				+ BR + "        name:   " + attribute.getTypeObject().getName());
	}

	private String toStringProperties(List<TObjectProperty> properties) {
		return properties.stream().map(this::toStringProperty)
				.collect(Collectors.joining(BR + "    --------------------------------------------------"));
	}

	private String toStringProperty(TObjectProperty property) {
		return ""
				+ BR + "    name:  " + property.getName()
				+ BR + "    value: " + property.getValue();
	}

	private String toStringPackage(TPackage tpackage) {
		return tpackage == null ? null : ""
				+ BR + "    name:   " + tpackage.getName()
				+ BR + "    eaGuid: " + tpackage.getEaGuid();
	}

	public TObjectProperty getPropertyByName(String name) {
		return getProperties().stream()
				.filter(p -> name.equals(p.getName())).findAny().orElse(null);
	}

	public TObjectProperty getPropertyByNameIgnoreCase(String name) {
		return getProperties().stream()
				.filter(p -> name.equalsIgnoreCase(p.getName())).findAny().orElse(null);
	}

	public String getPropertyValueByName(String name) {
		TObjectProperty prop = getPropertyByName(name);
		return prop == null ? null : prop.getValue();
	}

	public String getPropertyValueByNameIgnoreCase(String name) {
		TObjectProperty prop = getPropertyByNameIgnoreCase(name);
		return prop == null ? null : prop.getValue();
	}

	public TAttribute getAttributeByName(String name) {
		return getAttributes().stream()
				.filter(a -> {
					String attributeName = a.getName();
					return name.equals(attributeName);
				})
				.findFirst()
				.orElse(null);
	}

	public String getAttributeTypeByName(String name) {
		TAttribute attribute = getAttributeByName(name);
		return attribute == null ? null : attribute.getType();
	}
}
