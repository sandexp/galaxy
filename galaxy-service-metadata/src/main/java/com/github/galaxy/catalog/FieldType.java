package com.github.galaxy.catalog;

/**
 * 字段类型
 * 简单字段{@code SIMPLE_FIELD}
 * 简单表达式{@code SIMPLE_EXPRESSION}
 * 内置函数{@code OFFERED_FUNCTION}
 * 子查询{@code SUBQUERY}
 * 无类型{@code NONE} 缺省
 */
public enum FieldType {
	SIMPLE_FIELD,
	SIMPLE_EXPRESSION,
	OFFERED_FUNCTION,
	WINDOW_FUNCTION,
	SUBQUERY,
	NONE
}

