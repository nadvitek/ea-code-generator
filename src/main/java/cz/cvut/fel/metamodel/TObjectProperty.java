package cz.cvut.fel.metamodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Getter
@Setter

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
}
