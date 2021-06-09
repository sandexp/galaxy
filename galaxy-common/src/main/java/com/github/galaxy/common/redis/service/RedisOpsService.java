package com.github.galaxy.common.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Provide abstract redis service, you can use jedis or lettuce client to connect with
 * redis.
 */
@Slf4j
public abstract class RedisOpsService {

	protected RedisTemplate template;

	protected boolean initialized=false;

	@SuppressWarnings("uncheck")
	public Serializable get(Serializable key){
		Assert.isTrue(initialized);
		return (Serializable) template.opsForValue().get(key);
	}

	@SuppressWarnings("uncheck")
	public Boolean set(Serializable key,Serializable value){
		try {
			Assert.isTrue(initialized);
			template.opsForValue().set(key,value);
			return true;
		}catch (Exception cause){
			log.warn("Set {}:{} on failure. because {}",key,value,cause.getMessage());
			return false;
		}
	}

	@SuppressWarnings("uncheck")
	public boolean hasKey(Serializable key){
		Assert.isTrue(initialized);
		return template.hasKey(key);
	}

	@SuppressWarnings("uncheck")
	public long cacheInSet(Serializable key,Serializable... value){
		Assert.isTrue(initialized);
		return template.opsForSet().add(key,value);
	}

	@SuppressWarnings("uncheck")
	public void setExpire(Serializable key,long time){
		Assert.isTrue(initialized);
		template.expire(key,time, TimeUnit.SECONDS);
	}

	@SuppressWarnings("uncheck")
	public long getExpire(Serializable key){
		Assert.isTrue(initialized);
		return template.getExpire(key);
	}

	@SuppressWarnings("uncheck")
	public void delete(Serializable... key){
		Assert.isTrue(initialized);
		template.delete(key);
	}
}
