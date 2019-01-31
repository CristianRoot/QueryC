/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.function.BiFunction;

public interface Filter extends BiFunction<FromSupplier, CriteriaBuilder, Predicate> {

}
