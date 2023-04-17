package cz.cvut.fel.metamodel;

import cz.cvut.fel.util.ToStringProvider;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@Column(name = "ID")
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringProvider
				.getBuilder(this)
				.append("id", id)
				.toString();
	}

}