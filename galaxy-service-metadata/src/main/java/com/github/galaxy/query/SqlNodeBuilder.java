package com.github.galaxy.query;

import com.github.galaxy.annotation.ForTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.fun.SqlCase;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;

@Slf4j
public class SqlNodeBuilder implements Cloneable{

	private SqlNode sqlNode;

	@ForTest
	private SqlParser.Config defaultConfig ;

	private boolean legal=true;

	public SqlNodeBuilder(Lex lex,String sql) throws SqlParseException {
		defaultConfig=SqlParser.configBuilder().setLex(lex).build();
		SqlParser parser = SqlParser.create(sql, defaultConfig);
		this.sqlNode=parser.parseStmt();
	}

	@ForTest
	public SqlNodeBuilder(String sql) throws SqlParseException {
		this(Lex.MYSQL,sql);
	}

	public SqlNodeBuilder select(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode);
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder from(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getFrom();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder selectList(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getSelectList();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder where(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getWhere();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder having(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getHaving();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder orderList(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getOrderList();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder group(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getGroup();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder windowList(){
		if(sqlNode.getKind()== SqlKind.SELECT){
			sqlNode=((SqlSelect) sqlNode).getWindowList();
		}else{
			legal=false;
			log.warn("Can not get table info from sql.");
		}
		return this;
	}

	public SqlNodeBuilder left(){
		if(sqlNode.getKind()==SqlKind.JOIN){
			sqlNode=((SqlJoin) sqlNode).getLeft();
		}else {
			legal=false;
			log.warn("Can not get left join table from sql.");
		}
		return this;
	}

	public SqlNodeBuilder join(){
		if(sqlNode.getKind()==SqlKind.JOIN){
			sqlNode=((SqlJoin) sqlNode);
		}else {
			legal=false;
			log.warn("Can not get left join table from sql.");
		}
		return this;
	}

	public SqlNodeBuilder right(){
		if(sqlNode.getKind()==SqlKind.JOIN){
			sqlNode=((SqlJoin) sqlNode).getRight();
		}else {
			legal=false;
			log.warn("Can not get left join table from sql.");
		}
		return this;
	}

	public SqlNodeBuilder on(){
		if(sqlNode.getKind()==SqlKind.JOIN){
			sqlNode=((SqlJoin) sqlNode).getCondition();
		}else {
			legal=false;
			log.warn("Can not get left join table from sql.");
		}
		return this;
	}

	public JoinType joinType(){
		if(sqlNode.getKind()==SqlKind.JOIN){
			return ((SqlJoin) sqlNode).getJoinType();
		}else {
			return null;
		}
	}

	public SqlNodeBuilder insert(){
		if(sqlNode.getKind()==SqlKind.INSERT){
			sqlNode=((SqlInsert) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not an insert type.");
		}
		return this;
	}

	public SqlNodeBuilder insertTargetTable(){
		if(sqlNode.getKind()==SqlKind.INSERT){
			sqlNode=((SqlInsert) sqlNode).getTargetTable();
		}else {
			legal=false;
			log.warn("Given sql is not an insert type.");
		}
		return this;
	}

	public SqlNodeBuilder insertColumnList(){
		if(sqlNode.getKind()==SqlKind.INSERT){
			sqlNode=((SqlInsert) sqlNode).getTargetColumnList();
		}else {
			legal=false;
			log.warn("Given sql is not an insert type.");
		}
		return this;
	}

	public SqlNodeBuilder insertSource(){
		if(sqlNode.getKind()==SqlKind.INSERT){
			sqlNode=((SqlInsert) sqlNode).getSource();
		}else {
			legal=false;
			log.warn("Given sql is not an insert type.");
		}
		return this;
	}

	public SqlNodeBuilder update(){
		if(sqlNode.getKind()==SqlKind.UPDATE){
			sqlNode=((SqlUpdate) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not an update type.");
		}
		return this;
	}

	public SqlNodeBuilder updateCondition(){
		if(sqlNode.getKind()==SqlKind.UPDATE){
			sqlNode=((SqlUpdate) sqlNode).getCondition();
		}else {
			legal=false;
			log.warn("Given sql is not an update type.");
		}
		return this;
	}

	public SqlNodeBuilder updateTargetTable(){
		if(sqlNode.getKind()==SqlKind.UPDATE){
			sqlNode=((SqlUpdate) sqlNode).getTargetTable();
		}else {
			legal=false;
			log.warn("Given sql is not an update type.");
		}
		return this;
	}

	public SqlNodeBuilder updateColumnList(){
		if(sqlNode.getKind()==SqlKind.UPDATE){
			sqlNode=((SqlUpdate) sqlNode).getTargetColumnList();
		}else {
			legal=false;
			log.warn("Given sql is not an update type.");
		}
		return this;
	}

	public SqlNodeBuilder updateSourceList(){
		if(sqlNode.getKind()==SqlKind.UPDATE){
			sqlNode=((SqlUpdate) sqlNode).getSourceSelect();
		}else {
			legal=false;
			log.warn("Given sql is not an update type.");
		}
		return this;
	}

	public SqlNodeBuilder delete(){
		if(sqlNode.getKind()==SqlKind.DELETE){
			sqlNode=((SqlDelete) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not a delete type.");
		}
		return this;
	}

	public SqlNodeBuilder deleteTargetTable(){
		if(sqlNode.getKind()==SqlKind.DELETE){
			sqlNode=((SqlDelete) sqlNode).getTargetTable();
		}else {
			legal=false;
			log.warn("Given sql is not a delete type.");
		}
		return this;
	}

	public SqlNodeBuilder deleteCondition(){
		if(sqlNode.getKind()==SqlKind.DELETE){
			sqlNode=((SqlDelete) sqlNode).getCondition();
		}else {
			legal=false;
			log.warn("Given sql is not a delete type.");
		}
		return this;
	}

	public SqlNodeBuilder deleteSourceList(){
		if(sqlNode.getKind()==SqlKind.DELETE){
			sqlNode=((SqlDelete) sqlNode).getSourceSelect();
		}else {
			legal=false;
			log.warn("Given sql is not a delete type.");
		}
		return this;
	}

	public SqlNodeBuilder create(){
		if(sqlNode.getKind()==SqlKind.CREATE_TABLE){
			sqlNode=((SqlCreate) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not a create type.");
		}
		return this;
	}

	public SqlNodeBuilder drop(){
		if(sqlNode.getKind()==SqlKind.DROP_TABLE){
			sqlNode=((SqlDrop) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not a drop type.");
		}
		return this;
	}

	public SqlNodeBuilder desc(){
		if(sqlNode.getKind()==SqlKind.DESCRIBE_TABLE){
			sqlNode=((SqlDescribeTable) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not a desc type.");
		}
		return this;
	}

	public SqlNodeBuilder explain(){
		if(sqlNode.getKind()==SqlKind.EXPLAIN){
			sqlNode=((SqlExplain) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not an explain type.");
		}
		return this;
	}

	public SqlNodeBuilder identifier(){
		if(sqlNode.getKind()==SqlKind.IDENTIFIER){
			sqlNode=((SqlIdentifier) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not an identifier type.");
		}
		return this;
	}

	public SqlNodeBuilder orderBy(){
		if(sqlNode.getKind()==SqlKind.ORDER_BY){
			sqlNode=((SqlOrderBy) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not an order type.");
		}
		return this;
	}

	public SqlNodeBuilder window(){
		if(sqlNode.getKind()==SqlKind.WINDOW){
			sqlNode=((SqlWindow) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not a window type.");
		}
		return this;
	}

	public SqlNodeBuilder windowUpper(){
		if(sqlNode.getKind()==SqlKind.WINDOW){
			sqlNode=((SqlWindow) sqlNode).getUpperBound();
		}else {
			legal=false;
			log.warn("Given sql is not a window type.");
		}
		return this;
	}

	public SqlNodeBuilder windowLower(){
		if(sqlNode.getKind()==SqlKind.WINDOW){
			sqlNode=((SqlWindow) sqlNode).getLowerBound();
		}else {
			legal=false;
			log.warn("Given sql is not a window type.");
		}
		return this;
	}

	public SqlNodeBuilder caseNode(){
		if(sqlNode.getKind()==SqlKind.ORDER_BY){
			sqlNode=((SqlCase) sqlNode);
		}else {
			legal=false;
			log.warn("Given sql is not a case type.");
		}
		return this;
	}

	public SqlNodeBuilder caseElse(){
		if(sqlNode.getKind()==SqlKind.ORDER_BY){
			sqlNode=((SqlCase) sqlNode).getElseOperand();
		}else {
			legal=false;
			log.warn("Given sql is not a case type.");
		}
		return this;
	}

	public SqlNodeBuilder caseValue(){
		if(sqlNode.getKind()==SqlKind.ORDER_BY){
			sqlNode=((SqlCase) sqlNode).getValueOperand();
		}else {
			legal=false;
			log.warn("Given sql is not a case type.");
		}
		return this;
	}

	public SqlNodeBuilder caseWhen(){
		if(sqlNode.getKind()==SqlKind.ORDER_BY){
			sqlNode=((SqlCase) sqlNode).getWhenOperands();
		}else {
			legal=false;
			log.warn("Given sql is not a case type.");
		}
		return this;
	}

	public SqlNodeBuilder caseThen(){
		if(sqlNode.getKind()==SqlKind.ORDER_BY){
			sqlNode=((SqlCase) sqlNode).getThenOperands();
		}else {
			legal=false;
			log.warn("Given sql is not a case type.");
		}
		return this;
	}

	public SqlNode build(){
		if(!legal){
			log.warn("Sql Node chain is not illegal.");
			return null;
		}
		return sqlNode;
	}

}
