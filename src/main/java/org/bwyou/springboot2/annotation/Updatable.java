package org.bwyou.springboot2.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Updatable Field Check
 * 
 * @author yousky
 *
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Updatable {

	boolean isUpdatable() default true;	
}
