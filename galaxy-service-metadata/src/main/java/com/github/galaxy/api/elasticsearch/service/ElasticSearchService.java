package com.github.galaxy.api.elasticsearch.service;

import com.github.galaxy.api.elasticsearch.entity.ElasticSearchPipelineStatus;
import com.github.galaxy.api.elasticsearch.entity.ElasticSearchRecord;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public abstract class ElasticSearchService {


	protected NativeSearchQueryBuilder builder=null;

	protected ElasticSearchPipelineStatus status=ElasticSearchPipelineStatus.CLOSED;

	protected List<ElasticSearchPipelineStatus> searchChain=new ArrayList<>();

	public abstract boolean createIndex(Class klass);

	public abstract boolean deleteIndex(Class klass);

	public void init(){
		builder = new NativeSearchQueryBuilder();
		status=ElasticSearchPipelineStatus.INITIALIZED;
		searchChain.add(status);
	}

	public abstract boolean put(ElasticSearchRecord entity);

	public abstract boolean batch(List<ElasticSearchRecord> entitys);

	public abstract List fetchAll();

	public abstract ElasticSearchRecord getById(Long id);

	public abstract boolean updateById(Long id, Consumer updateLogic);

	public abstract void deleteById(Long id);

	public abstract Object getDocument();

	public boolean validate(){
		if(status==ElasticSearchPipelineStatus.CLOSED){
			log.warn("Can not search result when pipeline is not initialized.");
			return false;
		}
		if(status==ElasticSearchPipelineStatus.INITIALIZED){
			log.warn("There are not available data in pipeline. please check your search chain.");
			return false;
		}
		return true;
	}

	/**
	 * 按照指定逻辑匹配查询
	 * @param searchLogic 搜索逻辑
	 * @return {@link NativeSearchQueryBuilder}
	 */
	public ElasticSearchService search(QueryBuilder searchLogic){
		if(status==ElasticSearchPipelineStatus.CLOSED){
			log.warn("Prepare to init elasticsearch pipeline.");
			init();
		}
		if(searchLogic==null){
			log.warn("Given search logic is illegal. Please resend your request.");
			return null;
		}
		status=ElasticSearchPipelineStatus.SEARCHED;
		searchChain.add(status);
		builder=builder.withQuery(searchLogic);
		return this;
	}

	public ElasticSearchService filter(QueryBuilder filterLogic){
		if(!validate())
			return null;

		status=ElasticSearchPipelineStatus.FILTED;
		searchChain.add(status);
		builder=builder.withFilter(filterLogic);
		return this;
	}

	public ElasticSearchService sort(SortBuilder sortLogic){
		if(!validate())
			return null;
		status=ElasticSearchPipelineStatus.SORTED;
		searchChain.add(status);
		builder=builder.withSort(sortLogic);
		return this;
	}

	public ElasticSearchService page(Pageable pageable){
		if(!validate())
			return null;
		status=ElasticSearchPipelineStatus.PAGEABLE;
		searchChain.add(status);
		builder=builder.withPageable(pageable);
		return this;
	}

	public ElasticSearchService sourceFilter(SourceFilter filter){
		if(!validate())
			return null;
		status=ElasticSearchPipelineStatus.SOURCE_FILTER;
		searchChain.add(status);
		builder=builder.withSourceFilter(filter);
		return this;
	}

	public void printSearchChain(){
		if(searchChain.size()==0)
			return;
		StringBuffer buffer=new StringBuffer(searchChain.get(0).toString());
		for (int i = 1; i < searchChain.size(); i++)
			buffer.append(" -> ").append(searchChain.get(i));
		System.out.println(buffer.toString());
	}

	public List getSearchChain(){
		return searchChain;
	}
}
