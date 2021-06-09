package com.github.galaxy.common.jdbc;

/**
 * 用于构建SQL的DSL工具, 方便扩展统一DSL的java api.
 * 同时遵守mysql的语法逻辑, 可以用于存储过程的编写
 */
public class SQLBuilder {

	private StringBuffer sql;

	private static final String SPACE_SINGAL=" ";

	private static final String DELIMITED_CHAR=",";

	public SQLBuilder(){
		sql=new StringBuffer();
	}

	public SQLBuilder SELECT(String columns){
		sql.append("SELECT")
			.append(SPACE_SINGAL)
			.append(columns)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder SELECT(){
		sql.append("SELECT")
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder SELECT(String... columns){
		sql.append("SELECT").append(SPACE_SINGAL);
		for (String column:columns) {
			sql.append(column).append(DELIMITED_CHAR);
		}
		sql.deleteCharAt(sql.length()-1).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder UPDATE(String table){
		sql.append("UPDATE")
			.append(SPACE_SINGAL)
			.append(table)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder DELETE(String table){
		sql.append("DELETE")
			.append(SPACE_SINGAL)
			.append(table)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder INSERT_INTO(String table){
		sql.append("INSERT INTO")
			.append(SPACE_SINGAL)
			.append(table)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder SET(String key,String value){
		sql.append("SET")
			.append(SPACE_SINGAL)
			.append(key)
			.append(" = ")
			.append(value)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder SET(String... sets){
		if(sets.length%2!=0)
			return this;
		for (int i = 0; i < sets.length; i+=2) {
			sql.append("SET")
				.append(SPACE_SINGAL)
				.append(sets[i])
				.append(" = ")
				.append(sets[i+1])
				.append(DELIMITED_CHAR);
		}
		sql.deleteCharAt(sql.length()-1).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder INSERT_VALUE(String values){
		sql.append("VALUES")
			.append(SPACE_SINGAL)
			.append("(")
			.append(SPACE_SINGAL)
			.append(values)
			.append(SPACE_SINGAL)
			.append(")")
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder INSERT_VALUE(String... values){
		sql.append("VALUES")
			.append(SPACE_SINGAL)
			.append("(")
			.append(SPACE_SINGAL);
		for (String value:values) {
			sql.append(value).append(DELIMITED_CHAR);
		}
		sql.deleteCharAt(sql.length()-1)
			.append(SPACE_SINGAL)
			.append(")")
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder INSERT_COLUMNS(String columns){
		sql.append("(")
			.append(SPACE_SINGAL)
			.append(columns)
			.append(SPACE_SINGAL)
			.append(")")
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder XINSERT_COLUMNS(String... columns){
		sql.append("(").append(SPACE_SINGAL);
		for(String column:columns)
			sql.append(column).append(DELIMITED_CHAR);
		sql.deleteCharAt(sql.length()-1)
			.append(SPACE_SINGAL)
			.append(")")
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder FROM(String tables){
		sql.append("FROM").append(SPACE_SINGAL).append(tables).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder FROM(String... tables){
		sql.append("FROM").append(SPACE_SINGAL);
		for (String table:tables)
			sql.append(table).append(DELIMITED_CHAR);
		sql.deleteCharAt(sql.length()-1).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder AS(String alias){
		sql.append("AS")
			.append(SPACE_SINGAL)
			.append(alias)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder ON(){
		sql.append("ON").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder INNER_JOIN(String join){
		sql.append("INNER JOIN")
			.append(SPACE_SINGAL)
			.append(join)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder LEFT_OUTER_JOIN(String join){
		sql.append("LEFT JOIN")
			.append(SPACE_SINGAL)
			.append(join)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder RIGHT_OUTER_JOIN(String join){
		sql.append("RIGHT JOIN")
			.append(SPACE_SINGAL)
			.append(join)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder FULL_OUTER_JOIN(String join){
		sql.append("CROSS JOIN")
			.append(SPACE_SINGAL)
			.append(join)
			.append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder UNION(){
		sql.append("UNION").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder UNION_ALL(){
		sql.append("UNION ALL").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder WHERE(){
		sql.append("WHERE").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder OR(){
		sql.append("OR").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder AND(){
		sql.append("AND").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder GROUP_BY(String... columns){
		sql.append("GROUP BY").append(SPACE_SINGAL);
		for(String column:columns)
			sql.append(column).append(DELIMITED_CHAR);
		sql.deleteCharAt(sql.length()-1).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder ORDER_BY(String... columns){
		sql.append("ORDER BY").append(SPACE_SINGAL);
		for(String column:columns)
			sql.append(column).append(DELIMITED_CHAR);
		sql.deleteCharAt(sql.length()-1).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder HAVING(){
		sql.append("HAVING").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder DISTINCT(){
		sql.append("DISTINCT").append(SPACE_SINGAL);
		return this;
	}

	@Deprecated
	public SQLBuilder IN(){
		sql.append("IN").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder IN(String values){
		sql.append("IN").append(SPACE_SINGAL).append("(").append(values).append(")").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder NOT(){
		sql.append("NOT").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder IS_NULL(){
		sql.append("IS NULL").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder IS_NOT_NULL(){
		sql.append("IS NOT NULL").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder OVER_START(){
		sql.append("OVER").append(SPACE_SINGAL).append("(").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder OVER_END(){
		sql.append(")").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder PARTITION_BY(){
		sql.append("PARTITION BY").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder ASC(){
		sql.append("ASC").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder DESC(){
		sql.append("DESC").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder DENSE_RANK(){
		sql.append("DENSE_RANK()").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder ROW_NUMBER(){
		sql.append("ROW_NUMBER()").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder RANK(){
		sql.append("RANK()").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder COUNT(String columns){
		sql.append("COUNT(").append(columns).append(")").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder SUM(String columns){
		sql.append("SUM(").append(columns).append(")").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder IF_START(){
		sql.append("IF(").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder IF_END(){
		sql.append(")").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder SUB_START(){
		sql.append("(").append("\n");
		return this;
	}

	public SQLBuilder SUB_END(){
		sql.append(")").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder CASE_WHEN(String conditions){
		sql.append("CASE WHEN").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder THEN(){
		sql.append("THEN").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder ELSE(){
		sql.append("ELSE").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder END(){
		sql.append("END").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder appendAttribute(String attribute){
		sql.append(attribute).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder appendAttributes(String... attributes){
		for (String attribute:attributes)
			sql.append(attribute).append(DELIMITED_CHAR);
		sql.deleteCharAt(sql.length()-1).append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder EQUAL(){
		sql.append("=").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder NOT_EQUAL(){
		sql.append("<>").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder GREATER(){
		sql.append(">").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder LESS(){
		sql.append("<").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder LESS_AND_EQUAL(){
		sql.append("<=").append(SPACE_SINGAL);
		return this;
	}

	public SQLBuilder GREATER_AND_EQUAL(){
		sql.append(">=").append(SPACE_SINGAL);
		return this;
	}

	public String build(){
		return sql.deleteCharAt(sql.length()-1)
				.append(";")
				.toString();
	}

}
