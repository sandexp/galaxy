package com.github.galaxy.service;

import com.github.galaxy.catalog.FieldIdentifier;
import com.github.galaxy.catalog.Table;
import com.github.galaxy.common.jdbc.SQLBuilder;
import com.github.galaxy.common.jdbc.SQLExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 元数据信息查询服务
 * 1. 提供数据表的元数据检索
 * 2. 提供字段的元数据检索
 */
@Slf4j
@Service
public class MetaDataSearchService {

	@Autowired
	private SQLExecutor executor;


	///////////////////////////////////////////////////////////////////////////
	// Table search
	///////////////////////////////////////////////////////////////////////////
	public Table queryForTableInfo(String engine,String databaseName, String tableName){

		Object queryForObject = executor.queryForObject(
			new SQLBuilder()
				.SELECT("*")
				.FROM("`data_warehouse_metadata`")
				.WHERE()
				.appendAttribute("database_name")
				.EQUAL()
				.appendAttribute(databaseName)
				.AND()
				.appendAttribute("table_name")
				.EQUAL()
				.appendAttribute(tableName)
				.AND()
				.appendAttribute("table_engine")
				.EQUAL()
				.appendAttribute(engine)
		);

		try {
			return (Table) queryForObject;
		}catch (Exception e){
			log.error("{} can not be translate into Table.",queryForObject.toString());
			return null;
		}
	}

	public Table queryForTableInfo(String engine, String tableName){
		return queryForTableInfo(engine,"default",tableName);
	}

	public Table queryForTableInfo(String tableName){
		return queryForTableInfo("hive","default",tableName);
	}


	public Table queryForTableInfo(String engine, FieldIdentifier identifier){
		return queryForTableInfo(engine,identifier.getTable().getDatabase(),identifier.getTable().getTableName());
	}

	public Table queryForTableInfo(FieldIdentifier identifier){
		return queryForTableInfo("HIVE",identifier.getTable().getDatabase(),identifier.getTable().getTableName());
	}

	public List<Table> queryForTables(String engine,String databaseName){
		Object queryForObject = executor.queryForObject(
			new SQLBuilder()
			.SELECT("*")
			.FROM("`data_warehouse_metadata`")
			.WHERE()
			.appendAttribute("database_name")
			.EQUAL()
			.appendAttribute(databaseName)
			.AND()
			.appendAttribute("table_engine")
			.EQUAL()
			.appendAttribute(engine)
		);
		try {
			return (List<Table>) queryForObject;
		}catch (Exception e){
			log.error("{} can not be translate into Table.",queryForObject.toString());
			return null;
		}
	}


	///////////////////////////////////////////////////////////////////////////
	// TODO Field Search
	///////////////////////////////////////////////////////////////////////////


}
