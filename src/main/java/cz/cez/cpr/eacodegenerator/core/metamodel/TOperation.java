package cz.cez.cpr.eacodegenerator.core.metamodel;

import cz.cez.cpr.eacodegenerator.core.util.ToStringProvider;
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

	public TObject getObjectId() {
		return objectId;
	}

	public void setObjectId(TObject objectId) {
		this.objectId = objectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getClassifier() {
		return classifier;
	}

	public void setClassifier(Long classifier) {
		this.classifier = classifier;
	}

	public TObject getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(TObject typeObject) {
		this.typeObject = typeObject;
	}

	public List<TOperationParam> getParameters() {
		return parameters;
	}

	public void setParameters(List<TOperationParam> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", getId())
				.append("name", this.name)
				.toString();
	}
}
