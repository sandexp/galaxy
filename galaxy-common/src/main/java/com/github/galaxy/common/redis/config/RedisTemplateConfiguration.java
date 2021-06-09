package com.github.galaxy.common.redis.config;

import org.springframework.data.redis.core.RedisTemplate;

public abstract class RedisTemplateConfiguration {

	public abstract RedisTemplate getTemplate();
}
