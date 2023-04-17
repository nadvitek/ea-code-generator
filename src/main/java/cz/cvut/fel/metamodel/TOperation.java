package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
@Table(name = "t_operation")
@AttributeOverride(column = @Column(name = "OperationID"), name = "id")
public class TOperation extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name = "Object_ID")
	private TObject objectId;

	@Column(name = "Name")
	private String name;

	@Column(name = "Type")
	private String type;

	@Column(name = "Classifier")
	private Long classifier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Classifier", referencedColumnName = "Object_ID", updatable = false, insertable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private TObject typeObject;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "OperationID")
	private List<TOperationParam> parameters = new ArrayList<TOperationParam>();

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", getId())
				.append("name", this.name)
				.toString();
	}
}
