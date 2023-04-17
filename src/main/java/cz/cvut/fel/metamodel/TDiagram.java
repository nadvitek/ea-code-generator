package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter

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

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", getId())
				.append("name", name)
				.toString();
	}

}
