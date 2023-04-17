package cz.cvut.fel.metamodel;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.BooleanUtils;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Getter
@Setter

@Entity
@Table(name = "t_diagramlinks")
@AttributeOverride(column = @Column(name = "Instance_ID"), name = "id")
public class TDiagramLink extends AbstractEntity {

	@Column(name = "Hidden")
	Boolean hidden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ConnectorID")
	TConnector connector;
}
