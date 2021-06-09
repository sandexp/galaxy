package com.github.galaxy.common.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 通用SQL执行引擎, 用于执行sql
 */
@Slf4j
@Repository
public class SQLExecutor implements Cloneable {

	@Autowired
	private JdbcTemplate template;

	/**
	 * 执行更新操作, 用于JAVA API构建sql表达式
	 * @param builder
	 */
	public void executor(SQLBuilder builder){
		String sql=builder.build();
		executor(sql);
	}

	/**
	 * 执行更新操作, 用于sql表达式执行
	 * @param sql
	 */
	public void executor(String sql){
		try {
			template.execute(sql);
		}catch (Exception e){
			log.error("Execute {} on failure. due to {}.",sql,e.getMessage());
		}
	}

	/**
	 * 执行查找操作, 用于JAVA API的DSL表达式
	 * @param builder
	 * @return
	 */
	public List<Map<String, Object>> query(SQLBuilder builder){
		String sql=builder.build();
		return query(sql);
	}

	public List<Map<String, Object>> query(String sql){
		try {
			return template.queryForList(sql);
		}catch (Exception e){
			log.error("Execute {} on failure. due to {}.",sql,e.getMessage());
			return null;
		}
	}

	public Object queryForObject(SQLBuilder builder){
		String sql=builder.build();
		return queryForObject(sql);
	}

	public Object queryForObject(String sql){
		try {
			return template.queryForObject(sql,Object.class);
		}catch (Exception e){
			log.error("Execute {} on failure. due to {}.",sql,e.getMessage());
			return null;
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
