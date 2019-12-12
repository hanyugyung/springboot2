package org.bwyou.springboot2.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.bwyou.springboot2.annotation.Filterable;
import org.bwyou.springboot2.annotation.Updatable;
import org.bwyou.springboot2.dao.BWRepository;
import org.bwyou.springboot2.models.IdModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import net.kaczmarzyk.spring.data.jpa.domain.Conjunction;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.utils.Converter;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

public class IdEntityService<TEntity extends IdModel<TId>, TId> implements IEntityService<TEntity, TId> {

	protected BWRepository<TEntity, TId> daoRepository;
	
	public IdEntityService(BWRepository<TEntity, TId> daoRepository) {
		this.daoRepository = daoRepository;
	}

	@Override
	public List<TEntity> getList() {
    	return daoRepository.findAll();
	}

	@Override
	public List<TEntity> getList(String sort) {
		return daoRepository.findAll(GetOrderClause(sort));
	}

	@Override
	public Page<TEntity> getList(String sort, int pageNumber, int pageSize) {
		Pageable pageSpecification = PageRequest.of(pageNumber-1, pageSize, GetOrderClause(sort));
		return daoRepository.findAll(pageSpecification);
	}

	@Override
	public List<TEntity> getFilteredList(Specification<TEntity> spec) {
		return daoRepository.findAll(spec);
	}

	@Override
	public List<TEntity> getFilteredList(Specification<TEntity> spec, String sort) {
		return daoRepository.findAll(spec, GetOrderClause(sort));
	}

	@Override
	public Page<TEntity> getFilteredList(Specification<TEntity> spec, String sort, int pageNumber, int pageSize) {
		Pageable pageSpecification = PageRequest.of(pageNumber-1, pageSize, GetOrderClause(sort));
		return daoRepository.findAll(spec, pageSpecification);
	}

	@Override
	public Optional<TEntity> get(TId id) {
		return daoRepository.findById(id);
	}

	@Override
	public Optional<TEntity> get(Specification<TEntity> spec) {
		return daoRepository.findOne(spec);
	}

	@Override
	public TEntity validAndCreate(TEntity entity, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
        }
		return daoRepository.saveAndFlush(entity);
	}

	@Override
	public List<TEntity> validAndCreate(List<TEntity> entities, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
        }
		List<TEntity> result = daoRepository.saveAll(entities);
		daoRepository.flush();
		return result;
	}

	@Override
	public TEntity validAndUpdate(TId id, TEntity entity, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
        }
		if(entity.getId() != null && id != entity.getId()){
			bindingResult.addError(new FieldError(bindingResult.getObjectName(), "id","mismatch"));
            return null;
		}
		Optional<TEntity> optionalTarget = daoRepository.findById(id);
		if(optionalTarget.isPresent() == false){
			bindingResult.addError(new FieldError(bindingResult.getObjectName(), "id","Entity null"));
            return null;
		}
		TEntity target = optionalTarget.get();
		
		//TODO UpdateDT, CreateDT는 @EnableJpaAuditing을 사용하도록 해보자
		copyNonNullAndUpdatableProperties(entity, target, false, true, null);
		return daoRepository.saveAndFlush(target);
	}

	@Override
	public TEntity validAndDelete(TId id, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
        }
		Optional<TEntity> optionalTarget = daoRepository.findById(id);
		if(optionalTarget.isPresent() == false){
			bindingResult.addError(new FieldError(bindingResult.getObjectName(), "id","Entity null"));
            return null;
		}
		TEntity target = optionalTarget.get();
		daoRepository.delete(target);
		
		return target;
	}

	@Override
	public Iterable<TId> validAndDelete(Iterable<TId> ids, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return null;
        }
		List<TEntity> targets = daoRepository.findAllById(ids);
		if (targets == null) {
			return null;
        }
		daoRepository.deleteAll(targets);
		
		return ids;
	}

	public void copyNonNullAndUpdatableProperties(TEntity src, TEntity target, Boolean ignoreNull, Boolean updatableAttributeRequired, ArrayList<String> necessaryAddFields) {
	    BeanUtils.copyProperties(src, target, getNotUpdatePropertiesNameArray(src, ignoreNull, updatableAttributeRequired, necessaryAddFields));
	}
	
	/**
	 * updatable 할 필요 없는 field name들 반환
	 * @param source
	 * @param ignoreNull
	 * @param updatableAttributeRequired
	 * @param necessaryAddFields
	 * @return
	 */
	public String[] getNotUpdatePropertiesNameArray (TEntity source, Boolean ignoreNull, Boolean updatableAttributeRequired, ArrayList<String> necessaryAddFields) {
		//TODO 여기 잘 되는지 테스트 필요
	    final BeanWrapper src = new BeanWrapperImpl(source);

	    ArrayList<String> allFieldNames = new ArrayList<>();
	    ArrayList<String> updatableFieldNames = new ArrayList<String>();
	    ArrayList<String> notUpdatableFieldNames = new ArrayList<String>();
	    
		ReflectionUtils.doWithFields(source.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalAccessException {				
				ReflectionUtils.makeAccessible(field);

				String fieldName = field.getName();
				allFieldNames.add(fieldName);
                if (necessaryAddFields != null && necessaryAddFields.contains(fieldName))
                {
                	updatableFieldNames.add(fieldName);
                }else {
                	Updatable annotation = field.getAnnotation(Updatable.class);
    				if (annotation == null || annotation.isUpdatable() == false) {
    					notUpdatableFieldNames.add(fieldName);
    				} else {
    			        Object srcValue = src.getPropertyValue(fieldName);
    			        if(ignoreNull == false || srcValue != null) {
    			        	updatableFieldNames.add(fieldName);
    			        } else {
    			        	notUpdatableFieldNames.add(fieldName);
    			        }
    		    	}
                	
                }			
			}
		});

	    String[] result = new String[notUpdatableFieldNames.size()];
	    return notUpdatableFieldNames.toArray(result);
	}
	
    protected Converter defaultConverter = Converter.DEFAULT;
    
    protected QueryContext queryCtx = new QueryContext() {
	
		@Override
		public void putLazyVal(String key, Supplier<Object> value) {
		}
	
		@Override
		public Object getEvaluated(String key) {
			return null;
		}
	};
	protected Specification<TEntity> GetWhereClause(TEntity entity){

		final BeanWrapper src = new BeanWrapperImpl(entity);
	    Collection<Specification<TEntity>> innerSpecs = new ArrayList<Specification<TEntity>>();
		
		for (Field field : getAllFields(new ArrayList<Field>(), entity.getClass())) {
			//TODO 부모필드까지 올라 갔을 때 annotation 상속이나 override 고려 안 되어 있음.. 테스트 필요.
			if (field.isAnnotationPresent(Filterable.class) == true) {
				String name = field.getName();
				Object srcValue;
				try {
					srcValue = src.getPropertyValue(name);
				} catch (Throwable e) {
					e.printStackTrace();
					srcValue = null;
				}
				
				if (srcValue != null) {
					innerSpecs.add(new Equal<>(queryCtx, name, new String[]{srcValue.toString()}, defaultConverter));
				}
			}
		}
		
	    return new Conjunction<>(innerSpecs);
		
	}	

	public static Sort GetOrderClause(String sort){
		String[] sortInfoArray = sort.split(",");

		List<Order> orders = new ArrayList<>();
        for (String sortInfo : sortInfoArray)
        {
        	String sortProp = sortInfo;
        	boolean bDesc = false;
            if (sortInfo.startsWith("-") == true)
            {
                bDesc = true;
                sortProp = sortInfo.substring(1);
            }

            Order order = bDesc == false ? new Order(Direction.ASC, sortProp) : new Order(Direction.DESC, sortProp);
            
            orders.add(order);
        }

        return Sort.by(orders);
		
	}
	
	public List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    fields.addAll(Arrays.asList(type.getDeclaredFields()));

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}

	@Override
	public List<TEntity> getFilteredList(TEntity model) {
		return getFilteredList(GetWhereClause(model));
	}

	@Override
	public List<TEntity> getFilteredList(TEntity model, String sort) {
		return getFilteredList(GetWhereClause(model), sort);
	}

	@Override
	public Page<TEntity> getFilteredList(TEntity model, String sort, int pageNumber, int pageSize) {
		return getFilteredList(GetWhereClause(model), sort, pageNumber, pageSize);
	}	
}
