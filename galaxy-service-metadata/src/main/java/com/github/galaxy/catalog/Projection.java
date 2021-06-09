package com.github.galaxy.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xyf
 * @date 2021-04-10
 * 描述投影关系
 */
@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Projection {

	// 投影源字段
	private FieldIdentifier source;

	// 投影目标字段
	private FieldIdentifier target;

	// 投影关系描述
	private String expression;
}
