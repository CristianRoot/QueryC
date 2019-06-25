/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import com.cristianbreak.queryc.annotations.Ref;
import com.cristianbreak.queryc.constants.Direction;
import com.cristianbreak.queryc.exceptions.OrderFieldNotFoundException;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unused")
public class OrderExpressionFactory {

	private OrderExpressionFactory() {
	}

	public OrderExpression create(String field, Direction direccion, Class<?> entityClass) throws OrderFieldNotFoundException {
		OrderExpression orderExpression;
		String finalField = getField(field, entityClass);

		if (direccion == Direction.ASC) {
			orderExpression = (fs, cb) -> cb.asc(fs.apply(entityClass).get(finalField));
		} else {
			orderExpression = (fs, cb) -> cb.desc(fs.apply(entityClass).get(finalField));
		}

		return orderExpression;
	}

	private static String getField(String field, Class<?> entityClass) throws OrderFieldNotFoundException {
		try {
			Field f = entityClass.getDeclaredField(field);
			Ref ref = f.getAnnotation(Ref.class);
			if (Objects.nonNull(ref)) {
				// Check if the referenced class have the field
				entityClass.getDeclaredField(field);
				return ref.field();
			} else {
				return f.getName();
			}
		} catch (NoSuchFieldException e) {
			throw new OrderFieldNotFoundException(field);
		}
	}

}
