package com.github.galaxy.common.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

public class LettuceConfiguration extends RedisTemplateConfiguration {

	@Bean
	public RedisTemplate<Serializable, Serializable> lettuceTemplate(LettuceConnectionFactory connectionFactory) {
		RedisTemplate<Serializable, Serializable> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setConnectionFactory(connectionFactory);
		return redisTemplate;
	}

	@Override
	public RedisTemplate getTemplate() {
		return lettuceTemplate(new LettuceConnectionFactory());
	}
}
