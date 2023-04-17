package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter

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

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("operationId", this.operationId)
				.append("name", this.name)
				.toString();
	}
}
