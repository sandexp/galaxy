package com.github.galaxy.common.redis.service;

import com.github.galaxy.common.redis.config.JedisConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Provide jedis client to connect with redis server.
 */
@Slf4j
@Service
public class JedisOpsService extends RedisOpsService {

	@Autowired
	private JedisConfiguration configuration;

	@PostConstruct
	public void init(){
		super.template=configuration.getTemplate();
		super.initialized=true;
	}


}
