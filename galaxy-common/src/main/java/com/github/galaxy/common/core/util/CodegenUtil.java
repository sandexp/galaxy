package com.github.galaxy.common.core.util;

import com.github.galaxy.common.core.codegen.AbstractCodeGenerator;
import com.github.galaxy.common.core.codegen.PojoGenerator;
import com.github.galaxy.common.core.config.GeneratedTypeSuffix;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sandee
 * @date 2021-03-24
 * 代码生成器工具, 通过模板类生成pojo/xml
 */
@Slf4j
public class CodegenUtil {

	private static OutputStreamWriter writer;

	private static Template template;

	private static Configuration configuration;

	private static File pojoPath;


	private static void init(){
		configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		configuration.setDefaultEncoding(com.github.galaxy.common.core.config.Configuration.CODEGEN_CHARSET);
		pojoPath = new File(com.github.galaxy.common.core.config.Configuration.CODEGEN_SOURCES_DIR);
	}
	/**
	 * 自动写入代码
	 * @param suffix 文件类型后缀
	 * @param templateName 模板名称
	 * @param generator 生成器引用
	 * @throws Exception
	 */
	public static void autoGenerateCode(GeneratedTypeSuffix suffix,
	                                    String templateName,
	                                    AbstractCodeGenerator generator) throws Exception{
		init();
		configuration.setDirectoryForTemplateLoading(pojoPath);
		template= configuration.getTemplate(templateName);
		switch (suffix){

			case JAVA:
			{
				assert generator instanceof PojoGenerator;

				String tableName=((PojoGenerator) generator).getTableName();
				String[] splits = tableName.split("_");
				StringBuffer buffer=new StringBuffer();
				for (String split:splits)
					buffer.append(ParameterUtil.toCamelCase(split));

				String javaClassName = buffer.toString().concat(".java");
				String destination= com.github.galaxy.common.core.config.Configuration.CODEGEN_DESTINATION_DIR.concat(javaClassName);

				writer=new OutputStreamWriter(new FileOutputStream(destination));

				Map<String,Object> params=new HashMap<>();
				String pkgName=com.github.galaxy.common.core.config.Configuration.CODEGEN_DESTINATION_RELAVANT_DIR
						.split("/src/main/java/")[1].
						replace("/",".");
				params.put("pkg",pkgName.substring(0,pkgName.length()-1));
				params.put("className",javaClassName.split("\\.java")[0]);
				params.put("fields",((PojoGenerator) generator).getTermField());
				params.put("imports",new ArrayList<>());
				template.process(params,writer);
			}
			break;

			// TODO 之后有需求再做
			case XML:
			{

			}
			break;
		}
		log.info("Code has generated successfully.");
	}
}
