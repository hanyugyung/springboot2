package org.bwyou.springboot2.bindingmodels;

public interface IBindingModel<TEntity> {
	TEntity CreateBaseModel(TEntity src);
}
