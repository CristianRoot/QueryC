/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

@FunctionalInterface
public interface TriFunction<T, U, S, R> {
	R apply(T t, U u, S s);
}
