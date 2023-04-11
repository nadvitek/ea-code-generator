package cz.cez.cpr.eacodegenerator.core.metamodel;

import cz.cez.cpr.eacodegenerator.core.util.ToStringProvider;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_operationparams")
@IdClass(OperationParamPK.class)
public class TOperationParam {

	@Id()
	@Column(name = "OperationID")
	private Long operationId;

	@Id()
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

	@Column(name = "`Default`")
	private String initValue;

	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
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

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("operationId", this.operationId)
				.append("name", this.name)
				.toString();
	}
}
