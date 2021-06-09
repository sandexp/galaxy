package com.github.galaxy.common.core.entity;

import com.github.galaxy.common.core.config.CodeGenMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;


/**
 * 用于生成数据库POJO/XML的模板类, 使用代码生成器生成对应的数据库的Schema信息
 * 其中:
 * 1. 字段名称和字段类型,字段值需要对应起来.
 * 2. {@code pkgName} 表示包所在名称与{@code className} 组成全限定名称.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schema {

	private String tableName;

	private String pkgName;

	private String className;

	// 字段名称
	private LinkedHashSet<String> termNames;

	// 字段类型
	private LinkedHashSet<Class> termTypes;

	// 字段对应值
	private LinkedHashSet<Object> termValues;

	// 主键名称
	private LinkedHashSet<String> keys;

	private CodeGenMode mode;
}