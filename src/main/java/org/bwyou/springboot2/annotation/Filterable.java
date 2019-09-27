package org.bwyou.springboot2.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Filterable Field Check
 * 
 * @author yousky
 *
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Filterable {

	boolean isFilterable() default true;	
}
