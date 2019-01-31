/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import com.cristianbreak.queryc.annotations.Ref;
import com.cristianbreak.queryc.constants.Direction;
import com.cristianbreak.queryc.exceptions.OrderFieldNotFoundException;

import java.lang.reflect.Field;
import java.util.Objects;

public class OrderExpressionBuilder {

	private Class<?> entityClass;
	private String field;
	private int direccion;

	public OrderExpressionBuilder() {
	}

	public OrderExpressionBuilder(String field, int direccion, Class<?> entityClass) throws OrderFieldNotFoundException {
		this.direccion = direccion;
		this.entityClass = entityClass;
		this.setField(field);
	}

	public OrderExpression build() {
		OrderExpression orderExpression;

		if (this.getDireccion() == Direction.ASC) {
			orderExpression = (fs, cb) -> cb.asc(fs.apply(this.getEntityClass()).get(this.getField()));
		} else {
			orderExpression = (fs, cb) -> cb.desc(fs.apply(this.getEntityClass()).get(this.getField()));
		}

		return orderExpression;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) throws OrderFieldNotFoundException {
		try {
			Field f = this.getEntityClass().getDeclaredField(field);
			Ref ref = f.getAnnotation(Ref.class);
			if (Objects.nonNull(ref)) {
				// Check if the referenced class have the field
				this.getEntityClass().getDeclaredField(field);
				this.entityClass = ref.entity();
				this.field = ref.field();
			} else {
				this.entityClass = this.getEntityClass().getAnnotation(Ref.class).entity();
				this.field = f.getName();
			}
		} catch (NoSuchFieldException e) {
			throw new OrderFieldNotFoundException(field);
		}
	}

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}

}
