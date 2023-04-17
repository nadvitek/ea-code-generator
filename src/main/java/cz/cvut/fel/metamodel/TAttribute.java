package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Getter
@Setter

@Entity
@Table(name = "t_attribute")
public class TAttribute extends AbstractEntity {

	@Column(name = "Name")
	private String name;

	@Column(name = "Style")
	private String style;

	@Column(name = "Notes")
	private String notes;

	@Column(name = "Type")
	private String type;

	@Column(name = "Classifier")
	private Long classifier;

	@Column(name = "Stereotype")
	private String stereotype;

	@Column(name = "`Default`")
	private String initValue;

	@Column(name = "LowerBound")
	private String lowerBound;

	@Column(name = "UpperBound")
	private String upperBound;

	@ManyToOne
	@JoinColumn(name = "Object_ID")
	private TObject object;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Classifier", referencedColumnName = "Object_ID", updatable = false, insertable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private TObject typeObject;

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", getId())
				.append("name", name)
				.toString();
	}

}
