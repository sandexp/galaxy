package com.github.galaxy.exception;

/**
 * 元数据丢失出现的异常
 */
public class MetaDataNotFoundException extends RuntimeException {

	public MetaDataNotFoundException(String msg) {
		super(msg);
	}
}
