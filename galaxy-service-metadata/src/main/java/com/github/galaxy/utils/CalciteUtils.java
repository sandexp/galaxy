package com.github.galaxy.utils;

import com.github.galaxy.catalog.FieldIdentifier;
import com.github.galaxy.catalog.FieldType;
import com.github.galaxy.catalog.Table;
import com.github.galaxy.catalog.TableType;
import com.github.galaxy.common.jdbc.SQLBuilder;
import com.github.galaxy.config.Configuration;
import com.github.galaxy.exception.IllegalFormatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.commons.lang3.tuple.*;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 处理表元数据信息,以及相关表存储过程
 */
@Slf4j
public class CalciteUtils {

	private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	public static Table toTable(String str) throws IllegalFormatException {
		if(!str.contains("."))
			return null;

		String[] infos = str.split("\\.");

		TableType tableType=TableType.HIVE;
		String database="default",table="default";
		switch (infos.length){
			case 1:
				table=infos[0].contains("`")?infos[0].substring(1,infos[0].length()-1):infos[0];
			break;

			case 2:
				database=infos[0].contains("`")?infos[0].substring(1,infos[0].length()-1):infos[0];
				table=infos[1].contains("`")?infos[1].substring(1,infos[0].length()-1):infos[1];
			break;

			case 3:
				tableType=TableType.valueOf(infos[0].contains("`")?infos[0].substring(1,infos[0].length()-1):infos[0]);
				database=infos[1].contains("`")?infos[1].substring(1,infos[1].length()-1):infos[1];
				table=infos[2].contains("`")?infos[2].substring(1,infos[1].length()-1):infos[2];
			break;

			default:
				log.error("Illegal table format with {}.",str);
				throw new IllegalFormatException(String.format("%s has illegal format compared to tableType.database.tableName",str));
		}
		return new Table(tableType,database,table);
	}

	public static FieldIdentifier toIdentifier(String str) throws IllegalFormatException {
		if(!str.contains(".")){
			log.warn("{} have no table and database info.",str);
			return new FieldIdentifier(new Table(TableType.HIVE,"default","default"),str.trim());
		}
		String[] infos=str.split("\\.");
		String fieldName,tableStr;
		switch (infos.length){

			case 4:
				fieldName=infos[3].contains("`")?infos[3].substring(1,infos[0].length()-1):infos[3];
				tableStr=infos[0].concat(infos[1]).concat(infos[2]);
			break;

			case 3:
				fieldName=infos[2].contains("`")?infos[2].substring(1,infos[0].length()-1):infos[2];
				tableStr=infos[0].concat(infos[1]);
			break;

			case 2:
				fieldName=infos[1].contains("`")?infos[1].substring(1,infos[0].length()-1):infos[1];
				tableStr=infos[0];
			break;

			default:
				log.error("Illegal field format with {}",str);
				throw new IllegalFormatException(String.format("%s has illegal format compared to tableType.database.tableName.fieldName"));
		}
		return new FieldIdentifier(toTable(tableStr),fieldName);
	}

	///////////////////////////////////////////////////////////////////////////
	// 表处理工具
	///////////////////////////////////////////////////////////////////////////
	public static String format(String tableInfo){
		if(!tableInfo.contains("`"))
			return tableInfo;
		return tableInfo.split("`")[1];
	}

	public static String getName(String info){
		if(!info.contains("AS"))
			return info.trim();
		return info.split("AS")[0].trim();
	}

	public static String getAlias(String info){
		if(!info.contains("AS"))
			return null;
		return info.split("AS")[1].trim();
	}

	public static String trimAs(String str){
		if(!str.contains("AS"))
			return str.trim();
		int index = str.lastIndexOf("AS");
		return str.substring(0,index).trim();
	}

	public static String afterAs(String str){
		if(!str.contains("AS"))
			return "";
		int index = str.lastIndexOf("AS");
		return str.substring(index+2,str.length()).trim();
	}

	public static Pair<Table,String> splitFieldIdentifier(FieldIdentifier identifier){
		return new ImmutablePair(identifier.getTable(),identifier.getFieldName());
	}


	///////////////////////////////////////////////////////////////////////////
	// 判断类型类别
	///////////////////////////////////////////////////////////////////////////

	public static String getSubQuery(String str){
		str=str.trim();
		int start=str.indexOf("(")+1;
		int end=str.lastIndexOf(")");
		return str.substring(start,end).trim();
	}

	public static boolean isBasicOperate(SqlKind kind){
		return kind==SqlKind.PLUS
			|| kind==SqlKind.PLUS_PREFIX
			|| kind==SqlKind.MINUS
			|| kind==SqlKind.MINUS_PREFIX
			|| kind==SqlKind.FLOOR
			|| kind==SqlKind.CEIL
			|| kind==SqlKind.MOD
			|| kind==SqlKind.DIVIDE
			|| kind==SqlKind.TIMES;
	}

	public static boolean isOfferedFunction(SqlKind type){
		return SqlKind.FUNCTION.contains(type)
			|| SqlKind.AGGREGATE.contains(type)
			|| SqlKind.AVG_AGG_FUNCTIONS.contains(type)
			|| SqlKind.COVAR_AVG_AGG_FUNCTIONS.contains(type);
	}

	/**
	 * 解析字段的类型
	 * @param fieldInfo 字段表达式
	 * @return
	 */
	public static FieldType getFieldType(SqlNode fieldInfo){
		if(fieldInfo==null)
			return FieldType.NONE;
		// 标识类型仅仅提供简单投影
		if(fieldInfo.getKind()==SqlKind.IDENTIFIER || fieldInfo.getKind()==SqlKind.LITERAL){
			log.warn("It is not a good idea to fetch data without alias.");
			return FieldType.SIMPLE_FIELD;
		}

		if(fieldInfo.getKind()==SqlKind.SELECT){
			log.warn("It is not a good idea to fetch data without alias.");
			return FieldType.SUBQUERY;
		}

		if(isOfferedFunction(fieldInfo.getKind())){
			log.warn("It is not a good idea to fetch data without alias.");
			return FieldType.OFFERED_FUNCTION;
		}

		// AS类型提供复杂投影
		if(fieldInfo.getKind()== SqlKind.AS){
			SqlCall call= (SqlCall) fieldInfo;
			SqlKind type = call.getOperandList().get(0).getKind();

			if(type==SqlKind.IDENTIFIER){
				return FieldType.SIMPLE_FIELD;
			}else if(isOfferedFunction(type)){
				return FieldType.OFFERED_FUNCTION;
			}else if(SqlKind.SELECT==type){
				return FieldType.SUBQUERY;
			}else if(isBasicOperate(type)){
				return FieldType.SIMPLE_EXPRESSION;
			}else if(SqlKind.WINDOW==type){
				return FieldType.WINDOW_FUNCTION;
			}
			log.error("Can not find suitable type for this field.");
			return FieldType.NONE;

		}
		log.warn("Field info can not match basic sql principle. And will use common method to solve it. check your field info : {}",fieldInfo.toString());
		return FieldType.NONE;
	}

	///////////////////////////////////////////////////////////////////////////
	// 常用持久化方法
	///////////////////////////////////////////////////////////////////////////

	// FIXME: 2021/4/16 优化硬编码
	public static SQLBuilder insertIntoTableDependencyBuilder(String toTable, String fromTable){
		return new SQLBuilder()
			.INSERT_INTO("table_dependency")
			.INSERT_COLUMNS("`sub_table`,`parent_table`,`is_available`")
			.INSERT_VALUE(Configuration.SIGNAL_SQL_STRING +toTable+Configuration.SIGNAL_STRING_GAP+fromTable+"',1")
			.appendAttribute("ON DUPLICATE KEY")
			.UPDATE("update_time")
			.EQUAL()
			.appendAttribute("'"+dateFormat.format(new Date()).toString()+"'");
	}

	// FIXME: 2021/4/16 优化硬编码
	public static SQLBuilder insertIntoFieldDependencyBuilder(String upField, String downField,String expression){
		String word=Configuration.SIGNAL_SQL_STRING +upField+Configuration.SIGNAL_STRING_GAP+downField+Configuration.SIGNAL_STRING_GAP+expression+"',1";
		return new SQLBuilder()
			.INSERT_INTO("field_dependency")
			.INSERT_COLUMNS("`parent_level_field`,`sub_level_field`,`projection_expression`,`is_available`")
			.INSERT_VALUE(word)
			.appendAttribute("ON DUPLICATE KEY")
			.UPDATE("update_time")
			.EQUAL()
			.appendAttribute("'"+dateFormat.format(new Date()).toString()+"'");
	}
}
