package org.bwyou.springboot2.models;

public interface IIdModel<TId> extends IKeyModel {
	
	TId getId();
	void setId(TId id);
}
