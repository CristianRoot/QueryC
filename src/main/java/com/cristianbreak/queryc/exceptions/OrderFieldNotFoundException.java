/******************************************************************************
 * Copyright (c) 2019. QuantumStudio                                          *
 ******************************************************************************/

package com.cristianbreak.queryc.exceptions;

public class OrderFieldNotFoundException extends Exception {

	public OrderFieldNotFoundException(String field) {
		super("FIeld " + field + " not found. Can't order results with it.");
	}

}
