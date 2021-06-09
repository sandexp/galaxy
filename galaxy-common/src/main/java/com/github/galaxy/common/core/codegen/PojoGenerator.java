package com.github.galaxy.common.core.codegen;


import com.github.galaxy.common.core.config.CodeGenMode;
import com.github.galaxy.common.core.entity.Schema;
import com.github.galaxy.common.core.exception.RepeatFieldException;
import com.github.galaxy.common.core.exception.SchemaNotMatchException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 代码生成器
 */
@Data
@Slf4j
public class PojoGenerator extends AbstractCodeGenerator{

	private String tableName;

	private LinkedHashSet<Field> termField;

	private LinkedHashSet<String> keys;

	private Object lock=new Object();

	private CodeGenMode mode;

	// 通过Schema初始化 可能会出现竞争,但是不是热点数据,可以考虑锁优化
	public PojoGenerator(Schema schema) throws Exception{
		tableName=schema.getTableName();
		mode=schema.getMode();
		String[] termNames = (String[]) schema.getTermNames().toArray();
		Class[] termTypes = (Class[]) schema.getTermTypes().toArray();

		assert termNames.length==termTypes.length;

		termField=new LinkedHashSet<>();


		Set<Field> distinctSet=new HashSet<>();

		for (int i = 0; i < termNames.length; i++){
			Field field=new Field(termTypes[i].getName(),termNames[i]);
			if(distinctSet.contains(field))
				throw new RepeatFieldException("SQL table can not contains same field. please check your schema json.");
			termField.add(field);
		}

		distinctSet=null;
		log.info("Initialize persistent successfully.");

	}

	// 手动初始化构造器
	public PojoGenerator(){
		this.mode=CodeGenMode.POJO;
		this.tableName=null;
		this.termField=new LinkedHashSet<>();
		this.keys=new LinkedHashSet<>();
	}

	public PojoGenerator addFieldOnly(Field field){
		assert mode.equals(CodeGenMode.SCHEMA);
		termField.add(field);
		return this;
	}

	public PojoGenerator addField(Field field,Object value) throws Exception{
		if(!field.getFieldType().equals(value.getClass().getName()))
			throw new SchemaNotMatchException("Input kv data not match with type");
		this.termField.add(field);
		return this;
	}


	// 核对属性表和值表类型是否匹配
	@PostConstruct
	public void validate() throws Exception{
		if(mode.equals(CodeGenMode.SCHEMA)){
			log.warn("Codegen work in schema mode. can not use it to operate.");
			return;
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class Field{

		private String fieldType;

		private String fieldName;

		@Override
		public boolean equals(Object o){
			if(this==o)
				return true;
			if(!(o instanceof Field))
				return false;
			Field that= (Field) o;
			return Objects.equals(fieldName,that.getFieldName());
		}

		@Override
		public int hashCode() {
			return Objects.hash(fieldType,fieldName);
		}
	}

}
