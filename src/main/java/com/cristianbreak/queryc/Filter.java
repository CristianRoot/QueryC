/******************************************************************************
 * Copyright (c) 2018. QuantumStudio                                          *
 ******************************************************************************/

package com.cristianbreak.queryc;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.function.BiFunction;

public interface Filter extends BiFunction<FromSupplier, CriteriaBuilder, Predicate> {

}
