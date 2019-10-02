package org.bwyou.springboot2.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.bwyou.springboot2.annotation.Updatable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(value = { AuditingEntityListener.class })
@MappedSuperclass
public class BWModel<TId> extends IdModel<TId> implements ICUModel {
	
	@CreatedDate
    @Column(nullable = false, updatable=false)
	private LocalDateTime createDT;
	
	@LastModifiedDate
    @Column(nullable = false)
	@Updatable
	private LocalDateTime updateDT;
}
