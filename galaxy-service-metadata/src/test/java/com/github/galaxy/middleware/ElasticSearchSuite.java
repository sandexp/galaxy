package com.github.galaxy.middleware;

import com.alibaba.fastjson.JSONObject;
import com.github.galaxy.api.elasticsearch.entity.ElasticSearchRecord;
import com.github.galaxy.api.elasticsearch.service.ElasticSearchServiceV2;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

//@Component
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ElasticSearchSuite {
//
//	@Autowired
//	private ElasticSearchServiceV2 service;
//
//	@Autowired
//	private ElasticsearchRepository repository;
//
//	@Autowired
//	private ElasticsearchRestTemplate template;
//
//	@Test
//	public void createIndex() {
//		service.createIndex(ElasticSearchRecord.class);
//	}
//
//	@Test
//	public void deleteIndex() {
//		service.deleteIndex(ElasticSearchRecord.class);
//	}
//
//	@Test
//	public void testEsWrite() {
//		JSONObject object = JSONObject.parseObject("{\"name\":\"mark\",\"age\":18}");
//		ElasticSearchRecord record = new ElasticSearchRecord(4L, "vivo", object);
//		service.put(record);
//	}
//
//	@Test
//	public void testEsRead() {
//		System.out.println("*****************************");
//		System.out.println("RESULT -->" + service.getById(1L));
//		System.out.println("*****************************");
//	}
//
//	@Test
//	public void testEsBatch() {
//		List<ElasticSearchRecord> records = new ArrayList<>();
//		JSONObject pojo1 = JSONObject.parseObject("{\"name\":\"ketty\",\"age\":25}");
//		JSONObject pojo2 = JSONObject.parseObject("{\"name\":\"netty\",\"age\":21}");
//		records.add(new ElasticSearchRecord(2L, "SATA", pojo1));
//		records.add(new ElasticSearchRecord(3L, "MATA", pojo2));
//		service.batch(records);
//	}
//
//	@Test
//	public void testDelete() {
//		service.deleteById(3L);
//	}
//
//
//	@Test
//	public void testEsSearch() {
//		BoolQueryBuilder queryBuilder = new BoolQueryBuilder().must(new MatchQueryBuilder("name", "vivo"));
//		service.search(queryBuilder);
//		List<ElasticSearchRecord> documents = (List<ElasticSearchRecord>) service.getDocument();
//		documents.forEach(x -> System.out.println(x));
//		service.printSearchChain();
//	}
//
//	@Test
//	public void testEsSort() {
//		BoolQueryBuilder queryBuilder = new BoolQueryBuilder().must(new MatchQueryBuilder("name", "vivo"));
//	}
//
//	@Test
//	public void testEsPageable() {
//
//	}
}



