package com.github.galaxy.api.elasticsearch.service;

import com.github.galaxy.api.elasticsearch.entity.ElasticSearchPipelineStatus;
import com.github.galaxy.api.elasticsearch.entity.ElasticSearchRecord;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
//@Service
public class ElasticSearchServiceV2 extends ElasticSearchService{

	@Autowired
	private ElasticsearchRestTemplate template;

	private NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();

	private ElasticSearchPipelineStatus status=ElasticSearchPipelineStatus.CLOSED;

	private List<ElasticSearchPipelineStatus> searchChain=new ArrayList<>();

	private IndexQueryBuilder indexQueryBuilder=new IndexQueryBuilder();

	private Object lock=new Object();

	@Override
	public boolean createIndex(Class klass){
		return template.createIndex(klass);
	}

	@Override
	public boolean deleteIndex(Class index){
		return template.deleteIndex(index);
	}

	@Override
	public boolean put(ElasticSearchRecord entity) {
		try {
			template.index(indexQueryBuilder.withObject(entity).build());
			return true;
		}catch (Exception cause){
			log.warn("Put {} on failure.because {}",entity.toString(),cause.getMessage());
			return false;
		}
	}

	@Override
	public boolean batch(List<ElasticSearchRecord> entitys) {
		List<IndexQuery> queryList=new ArrayList<>();
		try {
			for (ElasticSearchRecord record: entitys) {
				queryList.add(indexQueryBuilder.withObject(record).build());
			}
			template.bulkIndex(queryList);
			return true;
		}catch (Exception e){
			log.warn("Batch put record on failure. because {}",e.getMessage());
			return false;
		}
	}

	@Override
	public List fetchAll() {
		return null;
	}

	@Override
	public ElasticSearchRecord getById(Long id) {
		GetQuery query = new GetQuery();
		query.setId(String.valueOf(id));
		return template.queryForObject(query,ElasticSearchRecord.class);
	}

	@Override
	public boolean updateById(Long id, Consumer updateLogic) {
		try {
			synchronized (lock){
				ElasticSearchRecord record =getById(id);
				if(record==null)
					log.info("Record of id = {} is null. Your update logic may have no effect.",id);
				updateLogic.accept(record);
				put(record);
			}
			return true;
		}catch (Exception cause){
			log.warn("Update record of id= {} on failure. because {}",id,cause.getMessage());
			return false;
		}
	}

	@Override
	public void deleteById(Long id) {
		template.delete(ElasticSearchRecord.class,String.valueOf(id));
	}

	@Override
	public Object getDocument() {
		List<ElasticSearchRecord> records = template.queryForList(builder.build(), ElasticSearchRecord.class);
		builder=null;
		status= ElasticSearchPipelineStatus.CLOSED;
		searchChain.add(status);
		return records;
	}


}
