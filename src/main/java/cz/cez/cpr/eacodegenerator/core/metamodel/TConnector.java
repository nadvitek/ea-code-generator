package cz.cez.cpr.eacodegenerator.core.metamodel;

import cz.cez.cpr.eacodegenerator.core.util.ToStringProvider;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSrcCard() {
		return srcCard;
	}

	public void setSrcCard(String srcCard) {
		this.srcCard = srcCard;
	}

	public String getDestCard() {
		return destCard;
	}

	public void setDestCard(String destCard) {
		this.destCard = destCard;
	}

	public TObject getStartObject() {
		return startObject;
	}

	public void setStartObject(TObject startObject) {
		this.startObject = startObject;
	}

	public TObject getEndObject() {
		return endObject;
	}

	public void setEndObject(TObject endObject) {
		this.endObject = endObject;
	}

	public void setSrcRole(String srcRole) {
		this.srcRole = srcRole;
	}

	public String getSrcRole() {
		return srcRole;
	}

	public void setDestRole(String destRole) {
		this.destRole = destRole;
	}

	public String getDestRole() {
		return destRole;
	}

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public TObject getPdata1() {
		return pdata1;
	}

	public void setPdata1(TObject pdata1) {
		this.pdata1 = pdata1;
	}

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("startObject", startObject)
				.append("endObject", endObject)
				.toString();
	}
}
