package cz.cez.cpr.eacodegenerator.core.metamodel;

import org.apache.commons.lang.BooleanUtils;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_diagramlinks")
@AttributeOverride(column = @Column(name = "Instance_ID"), name = "id")
public class TDiagramLink extends AbstractEntity {

	@Column(name = "Hidden")
	Boolean hidden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ConnectorID")
	TConnector connector;

	public boolean isHidden() {
		return BooleanUtils.toBoolean(hidden);
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public TConnector getConnector() {
		return connector;
	}

	public void setConnector(TConnector connector) {
		this.connector = connector;
	}
}
