package com.github.galaxy.common.core.config;

public class Configuration {


	public static final String ES_NAME="es.name";

	public static final String DEFAULT_ES_NAME="anonymous";


	public static final String SYSTEM_ROOT_PATH=System.getProperty("user.dir");

	public static final String CODEGEN_RELAVANT_DIR="/src/main/resources/template/";
	// 模板路径
	public static final String CODEGEN_SOURCES_DIR=SYSTEM_ROOT_PATH.concat(CODEGEN_RELAVANT_DIR);

	// pojo 模板文件名称
	public static final String CODEGEN_POJO_NAME="pojo.ftl";

	public static final String CODEGEN_DESTINATION_RELAVANT_DIR="/src/main/java/com/github/galaxy/common/core/pojo/";

	public static final String CODEGEN_DESTINATION_DIR=SYSTEM_ROOT_PATH.concat(CODEGEN_DESTINATION_RELAVANT_DIR);

	public static final String CODEGEN_CHARSET="UTF-8";


}
