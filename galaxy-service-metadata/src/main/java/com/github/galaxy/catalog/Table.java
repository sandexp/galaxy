package com.github.galaxy.catalog;

import com.github.galaxy.config.Configuration;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author xyf
 * @date 2021-04-08
 * 记录表和表的血缘关系,其中报表和数据集市的表类型为{@code APPLICATION}
 */
@Slf4j
public class Table {

	// TODO 解决自连接血缘分析时候死循环问题,自连接度量信息按原表处理
	private long version=1L;

	// 设置表类型主要是解决异构数据源join问题
	@Getter
	@Setter
	private TableType tableType;

	@Getter
	@Setter
	private String database;

	@Getter
	@Setter
	private String tableName;

	// 表注释属性
	@Getter
	@Setter
	private String comment="";


	@Getter
	private long queryCount=0;

	@Getter
	private long joinCount=0;

	@Getter
	private long aggragateCount=0;

	@Getter
	private Date createTime;

	public Table(String tableName){
		this(TableType.HIVE,"default",tableName,"");
	}

	public Table(TableType tableType,
	             String database,
	             String tableName,
	             String comment){
		this.tableType=tableType;
		this.tableName=tableName;
		this.database=database;
		this.comment=comment;
		this.createTime=new Date();
	}

	public Table(TableType tableType,
	             String database,
	             String tableName){
		this(tableType,database,tableName,"");
	}

	public Table(String database,String tableName){
		this(TableType.HIVE,database,tableName,"");
	}

	///////////////////////////////////////////////////////////////////////////
	// Metrics info update
	///////////////////////////////////////////////////////////////////////////
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

	public void addAggregateMetrics(){
		this.aggragateCount++;
	}

	public void addAggregateMetricsByValue(int value){
		this.aggragateCount+=value;
	}

	@Override
	public int hashCode() {
		return tableName.hashCode();
	}

	@Override
	public String toString() {
		return String.format("`%s`.`%s`.`%s`",tableType,database,tableName);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Table))
			return false;
		Table table= (Table) obj;
		return this.tableName.equals(table.tableName)
			&& this.version==table.version;
	}
}
