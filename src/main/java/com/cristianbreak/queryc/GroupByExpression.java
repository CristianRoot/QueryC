/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import java.util.function.BiFunction;

public interface GroupByExpression extends BiFunction<FromSupplier, CriteriaBuilder, Expression> {

}
