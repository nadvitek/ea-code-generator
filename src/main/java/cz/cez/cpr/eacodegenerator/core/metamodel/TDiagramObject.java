package cz.cez.cpr.eacodegenerator.core.metamodel;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_diagramobjects")
@AttributeOverride(column = @Column(name = "Instance_ID"), name = "id")
public class TDiagramObject extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Object_ID")
	private TObject object;

	public TObject getObject() {
		return object;
	}

	public void setObject(TObject object) {
		this.object = object;
	}


}
