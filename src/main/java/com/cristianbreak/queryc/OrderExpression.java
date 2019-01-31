/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import java.util.function.BiFunction;

public interface OrderExpression extends BiFunction<FromSupplier, CriteriaBuilder, Order> {

}
