package org.bwyou.springboot2.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BWRepository<TEntity, ID> extends JpaRepository<TEntity, ID>, JpaSpecificationExecutor<TEntity>{

}
