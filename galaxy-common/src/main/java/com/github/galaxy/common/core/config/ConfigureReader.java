package com.github.galaxy.common.core.config;

public class ConfigureReader {
	private static ConfigureReader reader = new ConfigureReader();

	public static ConfigureReader getReader() {
		return reader;
	}

	private ConfigureReader() {
	}

	public Object get(String key,Object defaultValue){
		String value = System.getProperty(key);
		return value==null?defaultValue:value;
	}
}
