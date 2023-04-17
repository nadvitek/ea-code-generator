package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
@Getter
@Setter

public final class OperationParamPK implements Serializable {

	private Long operationId;

	private String name;

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.operationId)
				.append(this.name)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof OperationParamPK)) return false;

		OperationParamPK op = (OperationParamPK) obj;
		return new EqualsBuilder()
				.append(this.operationId, op.operationId)
				.append(this.name, op.name)
				.isEquals();
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
