package com.github.galaxy.catalog;

import com.github.galaxy.config.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 表属性
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class FieldIdentifier {

	private Table table;

	private String fieldName;

	private int queryCount=0;

	private int joinCount=0;

	public FieldIdentifier(Table table,String fieldName){
		this.table=table;
		this.fieldName=fieldName;
	}

	@Override
	public String toString() {
		return String.format("`%s`.`%s`",table,fieldName);
	}

	public void addQueryMetrics(){
		this.queryCount++;
	}

	public void addQueryMetricsByValue(int value){
		this.queryCount+=value;
	}

	public void addJoinMetrics(){
		this.joinCount++;
	}

	public void addJoinMetricsByValue(int value){
		this.joinCount+=value;
	}
}
