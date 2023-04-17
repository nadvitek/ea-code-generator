package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
@Getter
@Setter

@Entity
@Table(name = "t_connector")
@AttributeOverride(column = @Column(name = "Connector_ID"), name = "id")
public class TConnector extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "Name")
	private String name;

	@Column(name = "Notes")
	private String note;

	@Column(name = "Direction")
	private String direction;

	@Column(name = "Connector_Type")
	private String type;

	@Column(name = "SourceCard")
	private String srcCard;

	@Column(name = "DestCard")
	private String destCard;

	@Column(name = "SourceRole")
	private String srcRole;

	@Column(name = "DestRole")
	private String destRole;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Start_Object_ID")
	private TObject startObject;

	@Column(name = "Stereotype")
	private String stereotype;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "End_Object_ID")
	private TObject endObject;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PDATA1")
	private TObject pdata1;

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("startObject", startObject)
				.append("endObject", endObject)
				.toString();
	}
}
