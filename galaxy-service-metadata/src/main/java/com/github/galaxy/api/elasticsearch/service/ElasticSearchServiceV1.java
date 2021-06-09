package com.github.galaxy.api.elasticsearch.service;

import com.github.galaxy.api.elasticsearch.entity.ElasticSearchPipelineStatus;
import com.github.galaxy.api.elasticsearch.entity.ElasticSearchRecord;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 适合于ElasticSearch 7.x一下版本的API
 */
@Slf4j
@Deprecated
//@Service
public class ElasticSearchServiceV1 extends ElasticSearchService{

	@Autowired
	private ElasticsearchRestTemplate template;

	@Autowired
	private ElasticEntityRepository repository;


	@Override
	public boolean createIndex(Class klass){
		return template.createIndex(klass);
	}

	@Override
	public boolean deleteIndex(Class index){
		return template.deleteIndex(index);
	}


	public boolean put(ElasticSearchRecord entity){
		try {
			repository.save(entity);
			return true;
		}catch (Exception cause){
			log.warn("Storage {} on failure because {}",entity.toString(),cause.getMessage());
			return false;
		}
	}

	public boolean batch(List<ElasticSearchRecord> entitys){
		try {
			repository.saveAll(entitys);
			return true;
		}catch (Exception cause){
			log.warn("Batch put records on failure. because {}",cause.getMessage());
			return false;
		}
	}

	public List fetchAll(){
		List list=new ArrayList<>();
		repository.findAll().forEach( x-> list.add(x));
		return list;
	}

	public ElasticSearchRecord getById(Long id){
		return repository.findById(id).get();
	}

	public boolean updateById(Long id, Consumer updateLogic){
		try {
			Optional<ElasticSearchRecord> value = repository.findById(id);
			updateLogic.accept(value.get());
			repository.save(value.get());
			return true;
		}catch (Exception cause){
			log.warn("Update {} on failure. because {}.",id.toString(),cause.getMessage());
			return false;
		}
	}

	public void deleteById(Long id){
		repository.deleteById(id);
	}

	@Override
	public Object getDocument() {
		Object records = repository.search(builder.build());
		builder=null;
		status= ElasticSearchPipelineStatus.CLOSED;
		searchChain.add(status);
		return records;
	}
}
