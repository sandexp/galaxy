package com.github.galaxy.common.core.exception;

public class RepeatFieldException extends Exception {

	private String msg;

	public RepeatFieldException(String s) {
		this.msg=s;
	}
}
