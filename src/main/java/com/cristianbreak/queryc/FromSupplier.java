/******************************************************************************
 * Copyright (c) 2018. QuantumStudio                                          *
 ******************************************************************************/

package com.cristianbreak.queryc;

import javax.persistence.criteria.From;
import java.util.function.Function;

public interface FromSupplier extends Function<Class, From> {

	<T> From<?, T> apply(Class<T> aClass);

}