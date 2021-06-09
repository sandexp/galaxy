package com.github.galaxy.api.elasticsearch.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.web.PagedResourcesAssembler;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.UUID;

// TODO 注解实现动态注入
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "template",type = "_doc",shards = 2)
public class ElasticSearchRecord implements Serializable {

	@Id
	private Long id;

	@Field(type = FieldType.Keyword)
	private String name;

	//please use json to transform a pojo
	@Field(type = FieldType.Nested)
	private Object context;

	@SuppressWarnings("uncheck")
	@Override
	public String toString(){
		return new StringBuffer()
				.append("Id : ")
				.append(id)
				.append("\tName: ")
				.append(name)
				.append("\tContext: \n")
				.append(context.toString())
				.append("\n")
				.toString();
	}
}
