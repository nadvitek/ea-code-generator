package cz.cez.cpr.eacodegenerator.core.metamodel;

import cz.cez.cpr.eacodegenerator.core.util.ToStringProvider;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "t_diagram")
@AttributeOverride(column = @Column(name = "Diagram_ID"), name = "id")
public class TDiagram extends AbstractEntity {

	@Column(name = "Name")
	private String name;

	@Column(name = "Stereotype")
	private String stereotype;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Package_ID")
	private TPackage tpackage;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "Diagram_Id")
	private List<TDiagramObject> diagramObjects = new ArrayList<TDiagramObject>();

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "DiagramId")
	private List<TDiagramLink> diagramLinks = new ArrayList<TDiagramLink>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public TPackage getTpackage() {
		return tpackage;
	}

	public void setTpackage(TPackage tpackage) {
		this.tpackage = tpackage;
	}

	public List<TDiagramObject> getDiagramObjects() {
		return diagramObjects;
	}

	public void setDiagramObjects(List<TDiagramObject> diagramObjects) {
		this.diagramObjects = diagramObjects;
	}

	public List<TDiagramLink> getDiagramLinks() {
		return diagramLinks;
	}

	public void setDiagramLinks(List<TDiagramLink> diagramLinks) {
		this.diagramLinks = diagramLinks;
	}

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", getId())
				.append("name", name)
				.toString();
	}

}
