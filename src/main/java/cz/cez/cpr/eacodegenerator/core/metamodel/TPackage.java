package cz.cez.cpr.eacodegenerator.core.metamodel;

import cz.cez.cpr.eacodegenerator.core.util.ToStringProvider;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "t_package")
@AttributeOverride(column = @Column(name = "Package_ID"), name = "id")
public class TPackage extends AbstractEntity {

	@Column(name = "Name")
	private String name;

	@Column(name = "Notes")
	private String notes;

	@Column(name = "ea_guid")
	private String eaGuid;

	@Column(name = "Parent_ID", insertable = false, updatable = false)
	private Long parentId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<TObject> objects = new ArrayList<TObject>();

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "Parent_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private TPackage parent;

	@Transient
	private List<TPackage> fullPathObjs;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes;
	}

	public void setParent(TPackage parent) {
		this.parent = parent;
	}

	public TPackage getParent() {
		return parent;
	}

	public List<TObject> getObjects() {
		return objects;
	}

	public void setObjects(List<TObject> objects) {
		this.objects = objects;
	}

	public List<TPackage> getFullPathObjs() {
		return fullPathObjs;
	}

	public String getEaGuid() {
		return eaGuid;
	}

	public void setEaGuid(String eaGuid) {
		this.eaGuid = eaGuid;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@PostLoad
	private void initFullPath() {
		this.fullPathObjs = new LinkedList<>();
		this.fullPathObjs.add(this);

		TPackage parentPackage = this.getParent();
		while (parentPackage != null) {
			this.fullPathObjs.add(0, parentPackage);
			parentPackage = parentPackage.getParent();
		}
	}

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", getId())
				.append("name", name)
				.toString();
	}
}
