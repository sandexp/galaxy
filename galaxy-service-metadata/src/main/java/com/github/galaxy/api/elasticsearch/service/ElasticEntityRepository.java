package com.github.galaxy.api.elasticsearch.service;

import com.github.galaxy.api.elasticsearch.entity.ElasticSearchRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Deprecated
public interface ElasticEntityRepository extends ElasticsearchRepository<ElasticSearchRecord,Long> {


}
