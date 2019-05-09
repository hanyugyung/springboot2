package org.bwyou.springboot2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;

public interface IEntityService<TEntity, TId> {
	List<TEntity> getList();
	List<TEntity> getList(String sort);
	Page<TEntity> getList(String sort, int pageNumber, int pageSize);
	List<TEntity> getFilteredList(Specification<TEntity> spec);
	List<TEntity> getFilteredList(Specification<TEntity> spec, String sort);
	Page<TEntity> getFilteredList(Specification<TEntity> spec, String sort, int pageNumber, int pageSize);
	
    TEntity get(TId id);
    TEntity get(Specification<TEntity> spec);
    TEntity validAndCreate(TEntity entity, BindingResult bindingResult);
    List<TEntity> validAndCreate(List<TEntity> entities, BindingResult bindingResult);
    TEntity validAndUpdate(TId id, TEntity entity, BindingResult bindingResult);
    TEntity validAndDelete(TId id, BindingResult bindingResult);
}
