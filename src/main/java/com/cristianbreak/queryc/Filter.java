/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

public interface Filter extends TriFunction<FromSupplier, CriteriaBuilder, CriteriaQuery, Predicate> {

}
