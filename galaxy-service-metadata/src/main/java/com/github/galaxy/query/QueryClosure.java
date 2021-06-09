package com.github.galaxy.query;

import com.github.galaxy.catalog.FieldIdentifier;
import com.github.galaxy.catalog.Table;
import com.github.galaxy.common.jdbc.SQLExecutor;
import com.github.galaxy.config.Configuration;
import com.github.galaxy.exception.SqlAnanalysisException;
import com.github.galaxy.utils.CalciteUtils;
import com.github.galaxy.utils.TransformUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示一个查询的闭包
 * 处理简单查询的一个实体
 */
@Slf4j
public class QueryClosure{

	// 子查询的范围
	@Getter
	@Setter
	private String closure;

	// 子查询的别名信息
	@Getter
	@Setter
	private String alias;

	@Getter
	@Setter
	private String uniqueId;

	// 闭包维护内部from list的别名映射表
	private Map<String,String> fromMap;

	public QueryClosure(String closure, String alias) throws NoSuchAlgorithmException {
		this.closure=closure;
		this.alias=alias;
		this.fromMap=new HashMap<>();
		this.uniqueId=TransformUtil.md5(closure);
	}

	@Override
	public String toString() {
		return closure+" AS "+alias;
	}

	@Override
	public int hashCode() {
		return closure.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof QueryClosure))
			return false;
		if(this.closure!=((QueryClosure) obj).closure)
			return false;
		if(this.alias!=((QueryClosure) obj).alias)
			return false;
		return true;
	}

	public boolean isShared(Object obj){
		if(!(obj instanceof QueryClosure))
			return false;
		if(this.closure!=((QueryClosure) obj).closure)
			return false;
		return true;
	}

	public void parseTableInfo(SQLExecutor executor) throws Exception {
		SqlNodeBuilder builder=new SqlNodeBuilder(closure);

		// parse table info
		SqlNode node = builder.from().build();

		if(node.getKind()==SqlKind.IDENTIFIER){
			log.info("Closure {} is a simple query sql.",closure);
			executor.executor(
				CalciteUtils.insertIntoTableDependencyBuilder(uniqueId,node.toString())
			);

			if(fromMap.containsKey(node.toString())){
				throw new SqlAnanalysisException("Can not allow same alias in same layer. Check your sql "+closure+" .");
			}
			fromMap.put(node.toString(),node.toString());

			return;
		}

		// 处理连接查询
		while(true){
			SqlNode infoNode=null;
			SqlJoin join=null;
			if(node.getKind()==SqlKind.JOIN){
				join= (SqlJoin) node;
				infoNode = join.getRight();
			}else {
				join=null;
				infoNode=node;
			}

			if(infoNode.getKind()==SqlKind.SELECT){
				throw new SqlAnanalysisException("Every table of join expression must have alias.");
			}

			// 处理隐式子查询
			if(infoNode.getKind()==SqlKind.AS){
				SqlCall splitCall= (SqlCall) infoNode;
				String alias=splitCall.getOperandList().get(1).toString();
				SqlNode expression=splitCall.getOperandList().get(0);

				switch (expression.getKind()){
					case IDENTIFIER:
						executor.executor(
							CalciteUtils.insertIntoTableDependencyBuilder(uniqueId,expression.toString())
						);
						if(fromMap.containsKey(alias)){
							throw new SqlAnanalysisException("Can not allow same alias in same layer. Check your sql "+closure+" .");
						}
						fromMap.put(alias,expression.toString());
						log.info("Get dependency from {} to {}.",expression.toString(),uniqueId);
					break;

					case SELECT:
						String upClosure=TransformUtil.md5(expression.toString());
						executor.executor(CalciteUtils.insertIntoTableDependencyBuilder(uniqueId,upClosure));
						log.info("Get dependency from sub-query {} to {}.",upClosure,uniqueId);

						QueryClosure closure = new QueryClosure(expression.toString(), alias);
						log.info("Parser function will be send to sub-closure {}.",upClosure);
						fromMap.put(alias,upClosure);
						closure.parseTableInfo(executor);
					break;

					default:
						log.warn("Sql\n : {} \ncan not be parsed successfully.", this.closure);
					break;
				}

			}
			if(join==null)
				break;
			node=join.getLeft();
		}
	}


	public void parseFields(SQLExecutor executor) throws Exception {

		SqlNodeBuilder builder=new SqlNodeBuilder(closure);

		SqlNodeList nodes = (SqlNodeList) builder.selectList().build();

		for (int i = 0; i < nodes.getList().size(); i++) {
			log.info("Deal with field {}.",nodes.get(i));
			String field=CalciteUtils.afterAs(nodes.get(i).toString());
			field=CalciteUtils.format(field);
			parserFieldInfo(executor,nodes.get(i),nodes.get(i),field);
		}
	}

	/**
	 * 字段级别血缘分析
	 * @param executor
	 * @param node 当前节点信息
	 * @param expression 父级表达式
	 * @throws Exception
	 */
	public void parserFieldInfo(SQLExecutor executor,SqlNode node,SqlNode expression,String fieldName) throws Exception{

		if(node==null){
			log.info("Parser stop on a stop point");
			return;
		}

		switch (CalciteUtils.getFieldType(node)){
			case SIMPLE_FIELD:
				FieldIdentifier fromIdentifier=null;
				if(node.getKind()==SqlKind.LITERAL){
					// ignore literal
					return;
				}

				String fromFieldName="";
				if(node.getKind()==SqlKind.IDENTIFIER){
					fromIdentifier=CalciteUtils.toIdentifier(node.toString());
					fromFieldName=CalciteUtils.getName(node.toString());
				}else {
					SqlCall callField= (SqlCall) node;

					fromFieldName=CalciteUtils.afterAs(node.toString());
					fromIdentifier = CalciteUtils.toIdentifier(callField.getOperandList().get(0).toString());

					if("".equals(fromFieldName)){
						fieldName=fromIdentifier.toString().split("\\.")[2];
					}

					fieldName=CalciteUtils.format(fromFieldName);
				}

				if(fromIdentifier==null){
					log.warn("Upstream identifier must not be null.");
					return;
				}

				if(fromIdentifier.getTable().getTableName().equals("default")
					|| null==fromIdentifier.getTable().getTableName()){
					log.warn("Table name can not be default or null.");
					return;
				}

				if(fromMap.containsKey(fromIdentifier.getTable().getTableName())){
					log.info("Get physics table info named {}.",fromMap.get(fromIdentifier.getTable().getTableName()));
					fromIdentifier.getTable().setTableName(fromMap.get(fromIdentifier.getTable().getTableName()));
				}

				// FIXME: 2021/4/19 设置默认数据库选项
				FieldIdentifier toIdentifier = new FieldIdentifier(new Table("default", TransformUtil.md5(closure)), fieldName);
				executor.executor(
					CalciteUtils.insertIntoFieldDependencyBuilder(fromIdentifier.toString(),toIdentifier.toString(),expression.toString())
				);
				log.info("Get field dependency {} -> {} successfully.",fromIdentifier.toString(),toIdentifier.toString());
			break;

			case SIMPLE_EXPRESSION:
				SqlBasicCall call= (SqlBasicCall) node;
				List<SqlNode> operandList = call.getOperandList();
				for (int j = 0; j < operandList.size(); j++) {
					SqlNode tmpNode = operandList.get(j);
					if(tmpNode.getKind()==SqlKind.AS){
						throw new SqlAnanalysisException("Simple expression can not contain an element with alias.");
					}
					log.info("Operate sub-element: {}. ",node);
					parserFieldInfo(executor,tmpNode,expression,fieldName);
				}
			break;

			case OFFERED_FUNCTION:
				SqlCall callFunc= (SqlCall) node;
				List<SqlNode> funcOperandList = callFunc.getOperandList();
				for (int i = 0; i < funcOperandList.size(); i++) {
					log.info("Operate sub-element: {}. ",funcOperandList.get(i));
					parserFieldInfo(executor,funcOperandList.get(i),expression,fieldName);
				}
				log.info("Function {} has been solved.",node.toString());
			break;

			case SUBQUERY:
				String alias=((SqlCall) node).getOperandList().get(1).toString();
				String subClosure=((SqlCall) node).getOperandList().get(0).toString();

				QueryClosure sub = new QueryClosure(subClosure, alias);
				log.info("Hand parser-work to sub-closure : {}.",subClosure);
				sub.parseFields(executor);
				log.info("Using sql closure to register sub-closure info: {}. ",subClosure);
				executor.executor(
					CalciteUtils.insertIntoFieldDependencyBuilder(
						TransformUtil.md5(subClosure).concat(Configuration.SIGNAL_DOT_CHAR).concat(alias),
						TransformUtil.md5(closure).concat(Configuration.SIGNAL_DOT_CHAR).concat(alias),
						node.toString()
					)
				);
			break;

			// 支持窗口函数功能
			case WINDOW_FUNCTION:
				SqlCall windowCall= (SqlCall) node;
				SqlNode windowNode = windowCall.getOperandList().get(0);
				SqlWindow window= (SqlWindow) windowNode;

				List<SqlNode> windowList = window.getOperandList();
				for (int i = 0; i < windowList.size(); i++) {
					log.info("Handler sub-node {} of window list.",windowList.get(i).toString());
					parserFieldInfo(executor,windowList.get(i),expression,fieldName);
				}

				SqlNodeList partitionList = window.getPartitionList();
				for (int i = 0; i < partitionList.size(); i++) {
					log.info("Handler sub-partition info {} of window list.",partitionList.get(i).toString());
					parserFieldInfo(executor,partitionList.get(i),expression,fieldName);
				}

				SqlNodeList orderList = window.getOrderList();
				for (int i = 0; i < orderList.size(); i++) {
					log.info("Handler sub-order info {} of window list.",orderList.get(i).toString());
					parserFieldInfo(executor,orderList.get(i),expression,fieldName);
				}

			break;

			default:
				log.info("Transform {}:{} to basic sql call.",node.getKind(),node.getClass().getName());
				if(node instanceof  SqlNodeList){
					for (int i = 0; i < ((SqlNodeList) node).size(); i++) {
						log.info("Find node {} of list type.",node.toString());
						parserFieldInfo(executor,((SqlNodeList) node).get(i),expression,fieldName);
					}
				}else {
					SqlCall defaultCall= (SqlCall) node;
					List<SqlNode> defaultOperandList = defaultCall.getOperandList();
					for (int i = 0; i < defaultOperandList.size(); i++) {
						log.info("Operate sub-element: {}. ",defaultOperandList.get(i));
						parserFieldInfo(executor,defaultOperandList.get(i),expression,fieldName);
					}
				}
			break;
		}
	}
}
