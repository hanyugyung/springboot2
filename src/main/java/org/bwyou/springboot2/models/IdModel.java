package org.bwyou.springboot2.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class IdModel<TId> implements IIdModel<TId> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private TId id;
    
	@Override
	public String getKeyName() {
		return "Id";
	}

	@Override
	public String toString() {
		return id.toString();
	}
}
