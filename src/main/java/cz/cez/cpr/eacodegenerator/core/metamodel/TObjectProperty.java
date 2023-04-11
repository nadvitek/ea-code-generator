package cz.cez.cpr.eacodegenerator.core.metamodel;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_objectproperties")
@AttributeOverride(column = @Column(name = "PropertyID"), name = "id")
public class TObjectProperty extends AbstractEntity {

	@Column(name = "Property")
	private String name;

	@Column(name = "Value")
	private String value;

	@Column(name = "Notes")
	private String note;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}


}
