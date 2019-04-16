package org.bwyou.springboot2.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;

public interface IEntityService<TEntity, TId> {
	List<TEntity> GetList();
	List<TEntity> GetList(String sort);
	Page<TEntity> GetList(String sort, int pageNumber, int pageSize);
	List<TEntity> GetFilteredList(Specification<TEntity> spec);
	List<TEntity> GetFilteredList(Specification<TEntity> spec, String sort);
	Page<TEntity> GetFilteredList(Specification<TEntity> spec, String sort, int pageNumber, int pageSize);
	
    TEntity Get(int id);
    TEntity Get(Specification<TEntity> spec);
    TEntity ValidAndCreate(TEntity entity, BindingResult bindingResult);
    List<TEntity> ValidAndCreate(List<TEntity> entities, BindingResult bindingResult);
    TEntity ValidAndUpdate(int id, TEntity entity, BindingResult bindingResult);
    int ValidAndDelete(int id, BindingResult bindingResult);
}
