package com.github.galaxy.common.core.exception;

public class SchemaNotMatchException extends Exception {

	private String msg;

	public SchemaNotMatchException(String msg){
		this.msg=msg;
	}

}
