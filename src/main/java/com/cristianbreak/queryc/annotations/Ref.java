/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ref {

	Class<?> entity() default Class.class;

	String field() default "";

}

