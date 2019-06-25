/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc.exceptions;

public class OrderFieldNotFoundException extends Exception {

	public OrderFieldNotFoundException(String field) {
		super("Field " + field + " not found. Can't order results with it.");
	}

}
