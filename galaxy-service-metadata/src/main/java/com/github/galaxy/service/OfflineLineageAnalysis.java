package com.github.galaxy.service;

import com.github.galaxy.common.jdbc.SQLExecutor;
import com.github.galaxy.common.redis.service.JedisOpsService;
import com.github.galaxy.query.QueryClosure;
import com.github.galaxy.query.SqlNodeBuilder;
import com.github.galaxy.utils.TransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.parser.SqlParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 血缘分析服务
 */
@Slf4j
@Service
public class OfflineLineageAnalysis extends LineageAnalysis {

	@Autowired
	private SQLExecutor executor;

	@Autowired
	private JedisOpsService redisOpsService;

	/**
	 * 该语句建立在数据仓库的元数据已经打通的情况下: 如果没有打通则会抛出异常
	 * {@code Insert}类型如果使用{@select 导入数据}会产生血缘关系,否则会使得操作表强制上线
	 * {@code Update}类型如果使用{@select 导入数据}会产生血缘关系,否则会使得操作表强制上线
	 * {@code Select}类型主要使用在数据应用层, 其连接到一个应用类型{@link com.github.galaxy.catalog.TableType}的表,
	 * 该表的元数据信息会同时注册到Redis和Mysql中
	 * {@code Drop}类型会强制是表下线
	 * 其他类型会修改表更新时间,所有操作都会记录操作类型
	 * 注意: 解析的过程中需要发现不满足数仓建模规范的sql语句
	 * @param sql sql
	 * @param lex 语法类型
	 * @throws SqlParseException
	 */
	public void parse(String sql, Lex lex) throws Exception {

		SqlNodeBuilder builder=new SqlNodeBuilder(lex, sql);

		try {

			switch (builder.build().getKind()){
				case SELECT:
					QueryClosure closure = new QueryClosure(sql, TransformUtil.md5(sql));
					closure.parseTableInfo(executor);
					closure.parseFields(executor);
				break;

				case INSERT:
					

				break;


				case UPDATE:

				break;

				case DROP_TABLE:

				break;

				default:
					log.info("SqlKind {} can not cause dependency.",builder.build().getKind());
				break;
			}
		}catch (Exception cause){
			log.error("Parser get an exception: {}.",cause.getMessage());
		}


	}

	public void parse(String sql) throws Exception {
		parse(sql,Lex.MYSQL);
	}

}
